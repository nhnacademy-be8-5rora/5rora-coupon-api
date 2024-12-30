package store.aurora.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import store.aurora.domain.*;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest    //repository 테스트용 어노테이션
@Transactional
class UserCouponRepositoryTest {

    @Autowired private UserCouponRepository userCouponRepository;
    @Autowired private CouponPolicyRepository couponPolicyRepository;
    @Autowired private DisCountRuleRepository discountRuleRepository;
    @Autowired private EntityManager entityManager;
    @Autowired private BookPolicyRepository bookPolicyRepository;
    @Autowired private CategoryPolicyRepository categoryPolicyRepository;

    // @BeforeAll은 static으로 선언되어야 하지만, @TestInstance(PER_CLASS)로 인스턴스 메서드처럼 사용할 수 있음
    @BeforeEach
    void setUp() {


        entityManager.clear();

        //계산법칙 생성
        DiscountRule discountRule = new DiscountRule();
        discountRule.setSaleAmount(10000L);
        discountRule.setNeedCost(20000L);
        discountRule = discountRuleRepository.save(discountRule);

        //쿠폰 정책 생성
        CouponPolicy couponPolicy = new CouponPolicy();
        couponPolicy.setName("Test Policy");
        couponPolicy.setSaleType(SaleType.AMOUNT);
        couponPolicy.setDiscountRule(discountRule);
        couponPolicy = couponPolicyRepository.save(couponPolicy);

        //사용자 카테고리 정책 생성
        UserCoupon coupon1 = new UserCoupon();
        coupon1.setUserId(1L);
        coupon1.setStartDate(LocalDate.now().minusDays(10));
        coupon1.setEndDate(LocalDate.now().minusDays(1));
        coupon1.setPolicy(couponPolicy);
        userCouponRepository.save(coupon1);

        UserCoupon coupon2 = new UserCoupon();
        coupon2.setUserId(2L);
        coupon2.setStartDate(LocalDate.now().minusDays(10));
        coupon2.setEndDate(LocalDate.now().plusDays(5));
        coupon2.setPolicy(couponPolicy);
        userCouponRepository.save(coupon2);

        //북 정책 생성
        BookPolicy bookPolicy1 = new BookPolicy();
        bookPolicy1.setBookId(1L); // Category 1
        bookPolicy1.setPolicy(couponPolicy);

        BookPolicy bookPolicy2 = new BookPolicy();
        bookPolicy2.setBookId(2L); // Category 2
        bookPolicy2.setPolicy(couponPolicy);

        bookPolicyRepository.save(bookPolicy1);
        bookPolicyRepository.save(bookPolicy2);

        //카테고리 정책 생성
        CategoryPolicy categoryPolicy1 = new CategoryPolicy();
        categoryPolicy1.setCategoryId(1L); // Category 1
        categoryPolicy1.setPolicy(couponPolicy);

        CategoryPolicy categoryPolicy2 = new CategoryPolicy();
        categoryPolicy2.setCategoryId(2L); // Category 2
        categoryPolicy2.setPolicy(couponPolicy);

        categoryPolicyRepository.save(categoryPolicy1);
        categoryPolicyRepository.save(categoryPolicy2);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testFindByUserId() {
        // UserId로 조회
        List<UserCoupon> coupons = userCouponRepository.findByUserId(1L);

        assertThat(coupons).hasSize(1);
        assertThat(coupons.getFirst().getUserId()).isEqualTo(1L);
    }

    @Test
    void testUpdateCouponStateByUserIds() {
        List<Long> userIds = List.of(1L, 2L);

        // UserIds에 대한 상태 업데이트
        userCouponRepository.updateCouponStateByUserIds(CouponState.TIMEOUT, userIds);

        entityManager.flush();
        entityManager.clear();

        List<UserCoupon> coupons = userCouponRepository.findByUserIdIn(userIds);

        assertThat(coupons).
                isNotEmpty().
                allMatch(coupon -> coupon.getCouponState() == CouponState.TIMEOUT);
    }

    @Test
    void testUpdateCouponPolicyByUserIds() {
        // Arrange
        CouponPolicy couponPolicy = new CouponPolicy();
        couponPolicy.setName("Test Policy");
        couponPolicy.setSaleType(SaleType.AMOUNT);

        // 할인 규칙을 couponPolicy에 연결
        DiscountRule discountRule = new DiscountRule();
        discountRule.setSaleAmount(10000);
        discountRule.setNeedCost(20000);
        discountRule = discountRuleRepository.save(discountRule);
        couponPolicy.setDiscountRule(discountRule);

        // couponPolicy 저장 후 ID 획득
        couponPolicy = couponPolicyRepository.save(couponPolicy);

        // 바뀔 policyId 사용
        Long newPolicyId = couponPolicy.getId();
        List<Long> userIds = List.of(1L);

        // Act
        userCouponRepository.updateCouponPolicyByUserIds(newPolicyId, userIds);

        entityManager.flush();
        entityManager.clear();

        // 바뀐 policyId로 검색
        List<UserCoupon> updatedCoupons = userCouponRepository.findAllByPolicyId(newPolicyId);

        assertThat(updatedCoupons).
                isNotEmpty().
                isNotNull();
    }

    @Test
    void testUpdateCouponEndDateByUserIds() {
        // Arrange
        LocalDate newEndDate = LocalDate.of(2024, 12, 31);
        List<Long> userIds = List.of(1L, 2L);

        // Act
        userCouponRepository.updateCouponEndDateByUserIds(newEndDate, userIds);

        entityManager.flush();
        entityManager.clear();

        // Assert
        List<UserCoupon> updatedCoupons = userCouponRepository.findAllByEndDate(newEndDate);
        assertThat(updatedCoupons)
                .isNotEmpty()
                .allSatisfy(coupon -> assertThat(coupon.getEndDate()).isEqualTo(newEndDate));
    }

    @Test
    void testUpdateExpiredCoupons() {
        // Given
        List<UserCoupon> couponsBeforeUpdate = userCouponRepository.findAll();
        assertThat(couponsBeforeUpdate)
                .isNotEmpty()
                .anyMatch(coupon -> coupon.getEndDate().isBefore(LocalDate.now()));

        // When
        userCouponRepository.updateExpiredCoupons();
        entityManager.flush();
        entityManager.clear();

        // Then
        List<UserCoupon> updatedCoupons = userCouponRepository.findAll();
        assertThat(updatedCoupons)
                .isNotEmpty()
                .allMatch(coupon -> {
                    if (coupon.getEndDate().isBefore(LocalDate.now())) {
                        return coupon.getCouponState() == CouponState.TIMEOUT;
                    }
                    return true;
                });
    }

    @Test
    void testDeleteExpiredCoupons() {
        // 만료된 쿠폰 삭제
        userCouponRepository.deleteExpiredCoupons(
                CouponState.USED,
                CouponState.TIMEOUT,
                LocalDate.now().minusDays(90)
        );

        entityManager.flush();
        entityManager.clear();

        List<UserCoupon> coupons = userCouponRepository.findAll();
        assertThat(coupons).hasSize(2); // 현재 테스트 데이터에는 조건에 맞는 삭제 없음
    }

    @Test
    void testFindAvailableCoupons() {
        long userId = 1L; // 테스트 사용자 ID
        long bookId = 4L; // 테스트 도서 ID
        List<Long> categoryIds = List.of(1L, 2L); // 테스트 카테고리 ID 리스트
        int totalPrice = 25000; // 테스트 가격

        List<UserCoupon> availableCoupons = userCouponRepository.
                findAvailableCoupons(userId, bookId, categoryIds, totalPrice);


        // 결과 확인
        assertThat(availableCoupons)
                .isNotNull() // null이 아님을 확인
                .isNotEmpty(); // 사용 가능한 쿠폰이 존재함을 확인

        userId = 2L;
        bookId = 3L;
        categoryIds = List.of(1L, 2L);
        totalPrice = 5000;

        List<UserCoupon> availableCoupons2 = userCouponRepository.
                findAvailableCoupons(userId, bookId, categoryIds, totalPrice);


        // 결과 확인
        assertThat(availableCoupons2)
                .isEmpty(); // 사용 가능한 쿠폰이 존재함을 확인
    }
}