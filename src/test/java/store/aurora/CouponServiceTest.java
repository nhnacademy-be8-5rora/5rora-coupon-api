package store.aurora;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
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
    @InjectMocks private CouponService couponService;

    private UserCoupon userCoupon1;
    private UserCoupon userCoupon2;

    @BeforeEach
    void setUp() {
        // Setting up mock UserCoupon objects before each test
        userCoupon1 = new UserCoupon();
        userCoupon1.setCouponId(1L);
        userCoupon1.setCouponState(CouponState.USED);
        userCoupon1.setUsedDate(java.time.LocalDate.now().minusDays(5));

        userCoupon2 = new UserCoupon();
        userCoupon2.setCouponId(2L);
        userCoupon2.setCouponState(CouponState.USED);
        userCoupon2.setUsedDate(java.time.LocalDate.now().minusDays(3));
    }

    @Test
    void testRefund_Success() {
        // Given: Setup mock behavior
        List<Long> userCouponIds = Arrays.asList(1L, 2L);
        when(couponRepository.findAllById(userCouponIds)).thenReturn(Arrays.asList(userCoupon1, userCoupon2));

        couponService.refund(userCouponIds);

        // Then: Verify the interactions and assert state changes
        verify(couponRepository).findAllById(userCouponIds);  // Verify repository was called
        verify(couponRepository).saveAll(anyList()); // Ensure that saveAll was called to persist changes

        // Verify state changes
        assert userCoupon1.getCouponState() == CouponState.LIVE;
        assert userCoupon1.getUsedDate() == null;

        assert userCoupon2.getCouponState() == CouponState.LIVE;
        assert userCoupon2.getUsedDate() == null;
    }

    @Test
    void testRefund_NoCouponsFound() {
        // Given: No coupons found for the provided IDs
        List<Long> userCouponIds = Arrays.asList(1L, 2L);
        when(couponRepository.findAllById(userCouponIds)).thenReturn(Arrays.asList());

        // When & Then: Expect an IllegalArgumentException
        try {
            couponService.refund(userCouponIds);
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("No coupons found for the provided IDs.");
        }

        verify(couponRepository).findAllById(userCouponIds);  // Ensure repository was called
        verifyNoMoreInteractions(couponRepository);  // No further interactions should occur
    }

    @Test
    void testRefund_CouponNotUsed() {
        // Given: Set up a coupon that is not used
        UserCoupon notUsedCoupon = new UserCoupon();
        notUsedCoupon.setCouponId(3L);
        notUsedCoupon.setCouponState(CouponState.LIVE);
        List<Long> userCouponIds = Arrays.asList(3L);
        when(couponRepository.findAllById(userCouponIds)).thenReturn(Arrays.asList(notUsedCoupon));

        // When & Then: Expect an IllegalStateException for trying to refund a not-used coupon
        try {
            couponService.refund(userCouponIds);
        } catch (IllegalStateException e) {
            assert e.getMessage().equals("Cannot refund not used coupon: ID = LIVE");
        }

        verify(couponRepository).findAllById(userCouponIds);  // Ensure repository was called
        verifyNoMoreInteractions(couponRepository);  // No further interactions should occur
    }
}