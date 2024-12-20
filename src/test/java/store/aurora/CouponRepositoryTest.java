package store.aurora;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import store.aurora.domain.*;
import store.aurora.repository.CouponPolicyRepository;
import store.aurora.repository.CouponRepository;
import store.aurora.repository.DisCountRuleRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // @BeforeAll에서 @Autowired 필드를 사용할 수 있도록 설정
class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    @Autowired
    private DisCountRuleRepository discountRuleRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeAll
    void beforeAll() {
        // @BeforeAll은 static으로 선언되어야 하지만, @TestInstance(PER_CLASS)로 인스턴스 메서드처럼 사용할 수 있음

        // discountRule 설정
        DiscountRule discountRule = new DiscountRule();
        discountRule.setSaleAmount(10000);
        discountRule = discountRuleRepository.save(discountRule);

        // CouponPolicy 설정
        CouponPolicy couponPolicy = new CouponPolicy();
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
        assertThat(coupons.get(0).getUserId()).isEqualTo(1L);
    }

    @Test
    void testUpdateCouponStateByUserIds() {
        List<Long> userIds = List.of(1L, 2L);

        // UserIds에 대한 상태 업데이트
        couponRepository.updateCouponStateByUserIds(CouponState.TIMEOUT, userIds);

        entityManager.flush();
        entityManager.clear();

        List<UserCoupon> coupons = couponRepository.findByUserIdIn(userIds);

        assertThat(coupons).isNotEmpty();
        assertThat(coupons).allMatch(coupon -> coupon.getCouponState() == CouponState.TIMEOUT);
    }

    @Test
    public void testUpdateCouponPolicyByUserIds() {
        // Arrange
        Long newPolicyId = 1L;
        List<Long> userIds = List.of(1L, 2L);

        // Act
        couponRepository.updateCouponPolicyByUserIds(newPolicyId, userIds);

        entityManager.flush();
        entityManager.clear();

        // Assert
        List<UserCoupon> updatedCoupons = couponRepository.findAllById(userIds);
        assertThat(updatedCoupons).allMatch(c -> c.getPolicy().getId().equals(1L));
    }

    @Test
    public void testUpdateCouponEndDateByUserIds() {
        // Arrange
        LocalDate newEndDate = LocalDate.of(2024, 12, 31);
        List<Long> userIds = List.of(1L, 2L);

        // Act
        couponRepository.updateCouponEndDateByUserIds(newEndDate, userIds);

        entityManager.flush();
        entityManager.clear();

        // Assert
        List<UserCoupon> updatedCoupons = couponRepository.findAllById(userIds);
        assertThat(updatedCoupons).allMatch(c -> c.getEndDate().equals(newEndDate));
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

