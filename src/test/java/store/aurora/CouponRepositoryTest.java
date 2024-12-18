package store.aurora;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import store.aurora.entity.*;
import store.aurora.repository.CouponPolicyRepository;
import store.aurora.repository.CouponRepository;
import store.aurora.repository.DisCountRuleRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    @Autowired
    private DisCountRuleRepository discountRuleRepository;

    @Autowired
    private EntityManager entityManager;

    private CouponPolicy couponPolicy;

    @BeforeEach
    void setUp() {
        //discountRule 설정
        DiscountRule discountRule = new DiscountRule();
        discountRule.setSaleAmount(10000);
        discountRule =discountRuleRepository.save(discountRule);

        // CouponPolicy 설정
        couponPolicy = new CouponPolicy();
        couponPolicy.setName("Test Policy");
        couponPolicy.setSaleType(SaleType.AMOUNT);  // SaleType 설정
        couponPolicy.setDiscountRule(discountRule);

        couponPolicy = couponPolicyRepository.save(couponPolicy);

        // UserCoupon 데이터 추가
        UserCoupon coupon1 = new UserCoupon();
        coupon1.setUserId(1L);  // userId 설정
        coupon1.setStartDate(LocalDate.now().minusDays(10));  // startDate 설정
        coupon1.setEndDate(LocalDate.now().minusDays(1));  // 만료된 상태
        coupon1.setPolicy(couponPolicy);  // CouponPolicy 설정

        UserCoupon coupon2 = new UserCoupon();
        coupon2.setUserId(2L);  // userId 설정
        coupon2.setStartDate(LocalDate.now().minusDays(10));  // startDate 설정
        coupon2.setEndDate(LocalDate.now().plusDays(5));  // 아직 만료되지 않음
        coupon2.setPolicy(couponPolicy);  // CouponPolicy 설정

        couponRepository.save(coupon1);
        couponRepository.save(coupon2);
    }

    @Test
    void testFindByUserId() {
        // UserId로 조회
        List<UserCoupon> coupons = couponRepository.findByUserId(1L);

        assertThat(coupons).hasSize(1);
        assertThat(coupons.getFirst().getUserId()).isEqualTo(1L);
    }

    @Test
    void testUpdateCouponStateByUserIds() {
        List<Long> userIds = List.of(1L, 2L);

        // UserIds에 대한 상태 업데이트
        int updatedRows = couponRepository.updateCouponStateByUserIds(CouponState.TIMEOUT, userIds);

        entityManager.flush();
        entityManager.clear();

        assertThat(updatedRows).isEqualTo(2);

        List<UserCoupon> coupons = couponRepository.findByUserIdIn(userIds);

        assertThat(coupons).isNotEmpty();
        assertThat(coupons).allMatch(coupon -> coupon.getCouponState() == CouponState.TIMEOUT);
    }

    @Test
    void testUpdateCouponAttributesByUserIds() {
        LocalDate startDate = LocalDate.now().minusDays(5);
        LocalDate endDate = LocalDate.now().plusDays(10);

        // UserCoupon 속성 업데이트(UserIds을 통한 사용자 쿠폰 수정)
        couponRepository.updateCouponAttributesByUserIds(
                CouponState.USED,
                couponPolicy.getId(),
                startDate,
                endDate,
                List.of(1L)
        );

        entityManager.flush();
        entityManager.clear();

        List<UserCoupon> coupons = couponRepository.findByUserId(1L);
        assertThat(coupons).isNotEmpty();
        assertThat(coupons).allMatch(coupon -> coupon.getCouponState() == CouponState.USED
                && coupon.getStartDate().equals(startDate)
                && coupon.getEndDate().equals(endDate));
    }

    @Test
    void testUpdateExpiredCoupons() {
        // 만료된 쿠폰 상태 업데이트
        couponRepository.updateExpiredCoupons();

        entityManager.flush();
        entityManager.clear();

        List<UserCoupon> coupons = couponRepository.findAll();

        assertThat(coupons).anyMatch(coupon -> coupon.getCouponState() == CouponState.TIMEOUT);
    }

    @Test
    void testDeleteExpiredCoupons() {
        // 만료된 쿠폰 삭제
        couponRepository.deleteExpiredCoupons(
                CouponState.USED,
                CouponState.TIMEOUT,
                LocalDate.now().minusDays(90)
        );

        entityManager.flush();
        entityManager.clear();

        List<UserCoupon> coupons = couponRepository.findAll();
        assertThat(coupons).hasSize(2); // 현재 테스트 데이터에는 조건에 맞는 삭제 없음
    }

}
