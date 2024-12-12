package store.aurora.repository;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import store.aurora.entity.CouponState;
import store.aurora.entity.UserCoupon;

import java.time.LocalDate;
import java.util.List;

public interface CouponRepository extends JpaRepository<UserCoupon, Long> {

    // userId 리스트로 UserCoupon 조회
    List<UserCoupon> findByUserIdIn(List<Long> userIds);

    @Modifying
    @Query("UPDATE UserCoupon uc SET uc.couponState = :newState WHERE uc.userId IN :userIds")
    int updateCouponStateByUserIds(String newState, List<Long> userIds);

//    @Modifying
//    @Query("UPDATE UserCoupon uc SET uc.couponState = :couponState, uc.expiryDate = " +
//            ":expiryDate WHERE uc.userId IN :userIds")
//    int updateCouponAttributesByUserIds(@Param("coupon_state") CouponState couponState,
//                                        @Param("start_date") LocalDate startDate,
//                                        @Param("end_date") LocalDate endDate);

    @Modifying
    @Query("UPDATE UserCoupon uc SET uc.couponState = :couponState, " +
            "uc.policy = (SELECT p FROM CouponPolicy p WHERE p.id = :policyId), " +
            "uc.startDate = :startDate, uc.endDate = :endDate " +
            "WHERE uc.userId IN :userIds")
    void updateCouponAttributesByUserIds(@Param("couponState") CouponState couponState,
                                        @Param("policyId") Long policyId,
                                        @Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate,
                                        @Param("userIds") List<Long> userIds);
}
