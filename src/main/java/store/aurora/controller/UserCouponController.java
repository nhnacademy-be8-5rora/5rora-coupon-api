package store.aurora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import store.aurora.entity.UserCoupon;
import store.aurora.service.CouponService;

@Controller
@RequestMapping("/coupon")
public class UserCouponController {

    @Autowired
    private final CouponService couponService;

    public UserCouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    //사용자 쿠폰 환불시
    @PutMapping(value = "/refund/{userCouponId{")
    public ResponseEntity<String> userCouponRefund(@PathVariable("userCouponId") Long userCouponId){
        if (userCouponId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User coupon not found.");
        }

        couponService.refund(userCouponId);

        return ResponseEntity.ok("Coupon refunded successfully.");
    }

    //사용자 쿠폰 사용시
    @PutMapping(value = "/using/{userCouponId{")
    public ResponseEntity<String> userCouponUsing(@PathVariable("userCouponId") Long userCouponId){
        if (userCouponId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User coupon not found.");
        }

        couponService.used(userCouponId);

        return ResponseEntity.ok("Coupon refunded successfully.");
    }
}
