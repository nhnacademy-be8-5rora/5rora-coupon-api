package store.aurora.repository;

import feign.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import store.aurora.entity.CouponState;
import store.aurora.entity.UserCoupon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CouponRepository extends JpaRepository<UserCoupon, Long> {

    // userId 리스트로 UserCoupon 조회
    List<UserCoupon> findByUserIdIn(List<Long> userIds);

    List<UserCoupon> findByUserId(Long userId);

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

    @Modifying
    @Transactional
    @Query("UPDATE UserCoupon u SET u.couponState = 'timeout', " +
            "    u.changedDate = CURRENT_TIMESTAMP " +
            "WHERE u.endDate < CURRENT_TIMESTAMP AND u.couponState = 'live'")
    void updateExpiredCoupons();

    @Modifying
    @Transactional
    @Query("DELETE FROM UserCoupon u " +
            "WHERE u.couponState IN (:usedState, :timeoutState) " +
            "AND u.changedDate < :ninetyDaysAgo")
    void deleteExpiredCoupons(
            @Param("usedState") CouponState usedState,
            @Param("timeoutState") CouponState timeoutState,
            @Param("ninetyDaysAgo") LocalDateTime ninetyDaysAgo);
}
