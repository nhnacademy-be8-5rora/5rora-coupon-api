package store.aurora;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.aurora.domain.CouponState;
import store.aurora.domain.UserCoupon;
import store.aurora.repository.CouponRepository;
import store.aurora.service.CouponService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // JUnit 5에서 Mockito 사용
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository; // Mock된 Repository

    @InjectMocks
    private CouponService couponService; // 테스트 대상 서비스

    private UserCoupon userCoupon1;
    private UserCoupon userCoupon2;

    @BeforeEach
    void setUp() {
        // 각 테스트 전에 사용자 쿠폰을 설정
        userCoupon1 = new UserCoupon();
        userCoupon1.setCouponId(1L);
        userCoupon1.setCouponState(CouponState.USED);
        userCoupon1.setChangedDate(LocalDate.now().minusDays(1));

        userCoupon2 = new UserCoupon();
        userCoupon2.setCouponId(2L);
        userCoupon2.setCouponState(CouponState.USED);
        userCoupon2.setChangedDate(LocalDate.now().minusDays(2));
    }

    @Test
    void testRefund_Success() {
        // given: 환불을 위한 ID 리스트
        List<Long> userCouponIds = Arrays.asList(1L, 2L);
        List<UserCoupon> userCoupons = Arrays.asList(userCoupon1, userCoupon2);

        // Mocking: repository에서 ID로 유저 쿠폰들을 찾아온다
        when(couponRepository.findAllById(userCouponIds)).thenReturn(userCoupons);

        // when: 환불 처리
        couponService.refund(userCouponIds);

        // then: 상태가 LIVE로 변경되었는지 검증
        assertAll(
                () -> assertEquals(CouponState.LIVE, userCoupon1.getCouponState()),
                () -> assertEquals(CouponState.LIVE, userCoupon2.getCouponState()),
                () -> assertNotNull(userCoupon1.getChangedDate()),
                () -> assertNotNull(userCoupon2.getChangedDate())
        );

        // verify: saveAll 메서드가 호출되었는지 검증
        verify(couponRepository, times(1)).saveAll(userCoupons);
    }

    @Test
    void testRefund_NoCouponsFound() {
        // given: 존재하지 않는 쿠폰 ID 리스트
        List<Long> userCouponIds = Arrays.asList(3L, 4L);

        // Mocking: repository에서 해당 ID로 유저 쿠폰을 찾을 수 없다고 설정
        when(couponRepository.findAllById(userCouponIds)).thenReturn(List.of());

        // when & then: 예외 발생 여부 확인
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            couponService.refund(userCouponIds);
        });

        assertEquals("No coupons found for the provided IDs.", thrown.getMessage());
    }

    @Test
    void testRefund_CouponNotUsed() {
        // given: 사용되지 않은 쿠폰 상태
        UserCoupon userCoupon3 = new UserCoupon();
        userCoupon3.setCouponId(3L);
        userCoupon3.setCouponState(CouponState.LIVE);
        userCoupon3.setChangedDate(LocalDate.now().minusDays(1));

        List<Long> userCouponIds = List.of(3L);
        List<UserCoupon> userCoupons = List.of(userCoupon3);

        // Mocking: repository에서 해당 쿠폰을 찾음
        when(couponRepository.findAllById(userCouponIds)).thenReturn(userCoupons);

        // when & then: 예외 발생 여부 확인
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            couponService.refund(userCouponIds);
        });

        assertEquals("Cannot refund not used coupon: ID = LIVE", thrown.getMessage());
    }
}