package store.aurora.repository;

import feign.Param;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import store.aurora.entity.CouponState;
import store.aurora.entity.UserCoupon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<UserCoupon, Long> {

    List<UserCoupon> findByUserId(Long userId);

    //관리자가 특정 사용자 ID 리스트에 해당하는 UserCoupon들의 couponState 업데이트
    @Modifying
    @Transactional
    @Query("UPDATE UserCoupon uc SET uc.couponState = :couponState WHERE uc.userId IN :userIds")
    int updateCouponStateByUserIds(@Param("couponState") CouponState couponState,
                                   @Param("userIds") List<Long> userIds);


    //사용자 쿠폰들 전체 수정
    @Modifying
    @Transactional
    @Query("UPDATE UserCoupon uc SET uc.couponState = :couponState, " +
            "uc.policy = (SELECT p FROM CouponPolicy p WHERE p.id = :policyId), " +
            "uc.startDate = :startDate, uc.endDate = :endDate " +
            "WHERE uc.userId IN :userIds")
    void updateCouponAttributesByUserIds(@Param("couponState") CouponState couponState,
                                        @Param("policyId") Long policyId,
                                        @Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate,
                                        @Param("userIds") List<Long> userIds);

    //timeout 이 된 사용자 쿠폰 상태 변경(live -> timeout)
    @Modifying
    @Transactional
    @Query("UPDATE UserCoupon u SET u.couponState = 'timeout', " +
            "    u.changedDate = CURRENT_DATE " +
            "WHERE u.endDate < CURRENT_DATE AND u.couponState = 'live'")
    void updateExpiredCoupons();

    //used, timeout 상태에서 90일이 지난  userCoupon 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM UserCoupon u " +
            "WHERE u.couponState IN (:usedState, :timeoutState) " +
            "AND u.changedDate < :ninetyDaysAgo")
    void deleteExpiredCoupons(
            @Param("usedState") CouponState usedState,
            @Param("timeoutState") CouponState timeoutState,
            @Param("ninetyDaysAgo") LocalDate ninetyDaysAgo);

    List<UserCoupon> findByUserIdIn(List<Long> userIds);
}
