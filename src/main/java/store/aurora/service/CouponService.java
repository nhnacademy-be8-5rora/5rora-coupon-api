package store.aurora.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import store.aurora.entity.CouponState;
import store.aurora.entity.UserCoupon;
import store.aurora.repository.CouponRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    //사용자 쿠폰 환불
    @Transactional
    public void refund(List<Long> userCouponId) {
        List<UserCoupon> userCoupons = couponRepository.findAllById(userCouponId);
        if (userCoupons.isEmpty()) {
            throw new IllegalArgumentException("No coupons found for the provided IDs.");
        }

        for (UserCoupon userCoupon : userCoupons) {
            if (userCoupon.getCouponState() != CouponState.USED) {
                throw new IllegalStateException("Cannot refund not used coupon: ID = " + userCoupon.getCouponState());
            }

            userCoupon.setCouponState(CouponState.LIVE);    //사용자 쿠폰 상태 live 변경(used -> live)
            userCoupon.setChangedDate(LocalDate.now());
        }

        couponRepository.saveAll(userCoupons); // 상태 변경 후 저장

    }

    //사용자 쿠폰 사용
    @Transactional
    public void used(List<Long> userCouponId) {
        List<UserCoupon> userCoupons = couponRepository.findAllById(userCouponId);
        if (userCoupons.isEmpty()) {
            throw new IllegalArgumentException("No coupons found for the provided IDs.");
        }

        for (UserCoupon userCoupon : userCoupons) {
            if (userCoupon.getCouponState() != CouponState.LIVE) {
                throw new IllegalStateException("Cannot use a dead coupon: ID = " + userCoupon.getCouponState());
            }

            userCoupon.setCouponState(CouponState.USED);    //Live -> used 변경
            userCoupon.setChangedDate(LocalDate.now());
        }

        couponRepository.saveAll(userCoupons); // 상태 변경 후 저장
    }
}
