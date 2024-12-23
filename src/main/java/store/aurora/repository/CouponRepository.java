package store.aurora.repository;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import store.aurora.domain.CouponState;
import store.aurora.domain.UserCoupon;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<UserCoupon, Long> {

    List<UserCoupon> findByUserId(Long userId);

    //관리자가 특정 사용자 ID 리스트에 해당하는 UserCoupon들의 couponState/endDate/policyId 업데이트
    @Modifying
    @Query("UPDATE UserCoupon uc SET uc.couponState = :couponState WHERE uc.userId IN :userIds")
    void updateCouponStateByUserIds(@Param("couponState") CouponState couponState,
                                   @Param("userIds") List<Long> userIds);
    @Modifying
    @Query("UPDATE UserCoupon uc SET uc.endDate = :endDate WHERE uc.userId IN :userIds")
    void updateCouponEndDateByUserIds(@Param("endDate") LocalDate endDate,
                                      @Param("userIds") List<Long> userIds);

    @Modifying
    @Query("UPDATE UserCoupon uc SET uc.policy = (SELECT p FROM CouponPolicy p WHERE p.id = :policyId) " +
            "WHERE uc.userId IN :userIds AND EXISTS (SELECT 1 FROM CouponPolicy p WHERE p.id = :policyId)")
    void updateCouponPolicyByUserIds(@Param("policyId") Long policyId,  //해당 policyId을 가진 policy 없을시 퀴리 적용 안되게함
                                     @Param("userIds") List<Long> userIds);

    //timeout 이 된 사용자 쿠폰 상태 변경(live -> timeout)
    @Modifying
    @Query("UPDATE UserCoupon u SET u.couponState = 'timeout', " +
            "    u.usedDate = CURRENT_DATE " +
            "WHERE u.endDate < CURRENT_DATE AND u.couponState = 'live'")
    void updateExpiredCoupons();

    //used, timeout 상태에서 90일이 지난  userCoupon 삭제
    @Modifying
    @Query("DELETE FROM UserCoupon u " +
            "WHERE u.couponState IN (:usedState, :timeoutState) " +
            "AND u.usedDate < :ninetyDaysAgo")
    void deleteExpiredCoupons(
            @Param("usedState") CouponState usedState,
            @Param("timeoutState") CouponState timeoutState,
            @Param("ninetyDaysAgo") LocalDate ninetyDaysAgo);

    List<UserCoupon> findByUserIdIn(List<Long> userIds);

    @Query("SELECT uc " +
            "FROM UserCoupon uc " +
            "JOIN uc.policy cp " +
            "LEFT JOIN cp.bookPolicies bp " +
            "LEFT JOIN cp.categoryPolicies cpCat " +
            "WHERE uc.userId = :userId " +
            "AND uc.couponState = 'LIVE' " +
            "AND (bp.bookId IS NULL OR bp.bookId IN :bookIds) " +
            "AND (cpCat.categoryId IS NULL OR cpCat.categoryId IN :categoryIds) " +
            "AND cp.discountRule.needCost <= :totalPrice")
    List<UserCoupon> findAvailableCoupons(@Param("userId") Long userId,
                                          @Param("bookIds") List<Long> bookIds,
                                          @Param("categoryIds") List<Long> categoryIds,
                                          @Param("totalPrice") Integer totalPrice);

}
