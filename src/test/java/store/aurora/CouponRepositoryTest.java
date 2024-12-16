//package store.aurora;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.transaction.annotation.Transactional;
//import store.aurora.entity.CouponPolicy;
//import store.aurora.entity.CouponState;
//import store.aurora.entity.UserCoupon;
//import store.aurora.repository.CouponPolicyRepository;
//import store.aurora.repository.CouponRepository;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@Transactional
//class CouponRepositoryTest {
//
//    @Autowired
//    private CouponRepository couponRepository;
//
//    @Autowired
//    private CouponPolicyRepository couponPolicyRepository;
//
//    private CouponPolicy couponPolicy;
//
//    @BeforeEach
//    void setUp() {
//        // CouponPolicy 설정
//        couponPolicy = new CouponPolicy();
//        couponPolicy.setName("Test Policy");
//        couponPolicy = couponPolicyRepository.save(couponPolicy);
//
//        // UserCoupon 데이터 추가
//        UserCoupon coupon1 = new UserCoupon();
//        coupon1.setUserId(1L);
//        coupon1.setCouponState(CouponState.LIVE);
//        coupon1.setStartDate(LocalDate.now().minusDays(10));
//        coupon1.setEndDate(LocalDate.now().minusDays(1)); // 만료된 상태
//        coupon1.setPolicy(couponPolicy);
//
//        UserCoupon coupon2 = new UserCoupon();
//        coupon2.setUserId(2L);
//        coupon2.setCouponState(CouponState.LIVE);
//        coupon2.setStartDate(LocalDate.now().minusDays(10));
//        coupon2.setEndDate(LocalDate.now().plusDays(5)); // 아직 만료되지 않음
//        coupon2.setPolicy(couponPolicy);
//        couponRepository.saveAll(List.of(coupon1, coupon2));
//    }
//
//    @Test
//    void testFindByUserId() {
//        // UserId로 조회
//        List<UserCoupon> coupons = couponRepository.findByUserId(1L);
//
//        assertThat(coupons).hasSize(1);
//        assertThat(coupons.getFirst().getUserId()).isEqualTo(1L);
//    }
//
//    @Test
//    void testUpdateCouponStateByUserIds() {
//        // UserIds에 대한 상태 업데이트
//        int updatedRows = couponRepository.updateCouponStateByUserIds(CouponState.TIMEOUT.name(), List.of(1L, 2L));
//
//        assertThat(updatedRows).isEqualTo(2);
//
//        List<UserCoupon> coupons = couponRepository.findAll();
//        assertThat(coupons).allMatch(coupon -> coupon.getCouponState() == CouponState.TIMEOUT);
//    }
//
//    @Test
//    void testUpdateCouponAttributesByUserIds() {
//        // UserCoupon 속성 업데이트
//        couponRepository.updateCouponAttributesByUserIds(
//                CouponState.USED,
//                couponPolicy.getId(),
//                LocalDate.now().minusDays(5),
//                LocalDate.now().plusDays(10),
//                List.of(1L)
//        );
//
//        UserCoupon updatedCoupon = couponRepository.findById(1L).orElseThrow();
//        assertThat(updatedCoupon.getCouponState()).isEqualTo(CouponState.USED);
//        assertThat(updatedCoupon.getStartDate()).isEqualTo(LocalDate.now().minusDays(5));
//        assertThat(updatedCoupon.getEndDate()).isEqualTo(LocalDate.now().plusDays(10));
//    }
//
//    @Test
//    void testUpdateExpiredCoupons() {
//        // 만료된 쿠폰 상태 업데이트
//        couponRepository.updateExpiredCoupons();
//
//        List<UserCoupon> coupons = couponRepository.findAll();
//        assertThat(coupons).anyMatch(coupon -> coupon.getCouponState() == CouponState.TIMEOUT);
//    }
//
//    @Test
//    void testDeleteExpiredCoupons() {
//        // 만료된 쿠폰 삭제
//        couponRepository.deleteExpiredCoupons(
//                CouponState.USED,
//                CouponState.TIMEOUT,
//                LocalDateTime.now().minusDays(90)
//        );
//
//        List<UserCoupon> coupons = couponRepository.findAll();
//        assertThat(coupons).hasSize(2); // 현재 테스트 데이터에는 조건에 맞는 삭제 없음
//    }
//}
