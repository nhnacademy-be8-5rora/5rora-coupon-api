package store.aurora;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import store.aurora.domain.CouponState;
import store.aurora.domain.UserCoupon;
import store.aurora.repository.CouponRepository;
import store.aurora.service.CouponService;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class CouponServiceTest {

    @MockBean
    private CouponRepository couponRepository;

    @Autowired
    private CouponService couponService;

    private UserCoupon userCoupon1;
    private UserCoupon userCoupon2;

    @BeforeEach
    void setUp() {
        // Setting up mock UserCoupon objects before each test
        userCoupon1 = new UserCoupon();
        userCoupon1.setCouponId(1L);
        userCoupon1.setCouponState(CouponState.USED);
        userCoupon1.setUserId(1L);
        userCoupon1.setUsedDate(java.time.LocalDate.now().minusDays(5));

        userCoupon2 = new UserCoupon();
        userCoupon2.setCouponId(2L);
        userCoupon2.setCouponState(CouponState.USED);
        userCoupon2.setUserId(2L);
        userCoupon2.setUsedDate(java.time.LocalDate.now().minusDays(3));
    }

    @Test
    void testRefund_Success() {
        // Given: Setup mock behavior
        List<Long> userCouponIds = Arrays.asList(1L, 2L);
        when(couponRepository.findAllById(userCouponIds)).thenReturn(Arrays.asList(userCoupon1, userCoupon2));

        // When: Execute refund
        couponService.refund(userCouponIds);

        // Then: Verify the interactions and assert state changes
        verify(couponRepository).findAllById(userCouponIds);  // Verify repository was called
        verify(couponRepository).saveAll(anyList()); // Ensure that saveAll was called to persist changes

        // Verify state changes using assertThat
        assertThat(userCoupon1.getCouponState()).isEqualTo(CouponState.LIVE);
        assertThat(userCoupon1.getUsedDate()).isNull();

        assertThat(userCoupon2.getCouponState()).isEqualTo(CouponState.LIVE);
        assertThat(userCoupon2.getUsedDate()).isNull();
    }

    @Test
    void testRefund_NoCouponsFound() {
        List<Long> userCouponIds = Arrays.asList(1L, 2L);

        //findAllById 실행시에 빈 리스트 return
        when(couponRepository.findAllById(userCouponIds)).thenReturn(List.of());

        // When & Then: Expect an IllegalArgumentException
        assertThatThrownBy(() -> couponService.refund(userCouponIds))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No coupons found for the provided IDs.");

        verify(couponRepository).findAllById(userCouponIds);
    }

    @Test
    void testRefund_CouponNotUsed() {
        // Given: Set up a coupon that is not used
        UserCoupon notUsedCoupon = new UserCoupon();
        notUsedCoupon.setCouponId(3L);
        notUsedCoupon.setCouponState(CouponState.LIVE);
        List<Long> userCouponIds = List.of(3L);
        when(couponRepository.findAllById(userCouponIds)).thenReturn(List.of(notUsedCoupon));

        // When & Then: Expect an IllegalStateException for trying to refund a not-used coupon
        assertThatThrownBy(() -> couponService.refund(userCouponIds))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot refund not used coupon: ID = LIVE");

        verify(couponRepository).findAllById(userCouponIds);  // Ensure repository was called
        verifyNoMoreInteractions(couponRepository);  // No further interactions should occur
    }
}