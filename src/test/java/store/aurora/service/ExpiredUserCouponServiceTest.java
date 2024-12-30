package store.aurora.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import store.aurora.domain.CouponState;
import store.aurora.repository.UserCouponRepository;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ExpiredUserCouponServiceTest {

    @MockBean
    private UserCouponRepository userCouponRepository;

    @Autowired
    private ExpiredUserCouponService expiredUserCouponService;

    @Test
    void testUpdateExpiredCoupons() {
        // Given: 쿠폰 만료 상태 업데이트 메서드 호출 전, 상태에 대한 설정

        // When: 만료된 쿠폰을 업데이트하는 메서드를 호출
        expiredUserCouponService.updateExpiredCoupons();

        // Then: couponRepository.updateExpiredCoupons()가 호출된 것을 검증
        verify(userCouponRepository, times(1)).updateExpiredCoupons();
    }

    @Test
    void testDeleteExpiredCoupons() {
        // Given: 쿠폰 삭제 시나리오에 대한 설정
        LocalDate ninetyDaysAgo = LocalDate.now().minusDays(90);

        // When: 만료된 쿠폰을 삭제하는 메서드를 호출
        expiredUserCouponService.deleteExpiredCoupons();

        // Then: couponRepository.deleteExpiredCoupons()가 호출된 것을 검증
        verify(userCouponRepository, times(1))
                .deleteExpiredCoupons(CouponState.USED, CouponState.TIMEOUT, ninetyDaysAgo);

    }


}
