package store.aurora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import store.aurora.entity.UserCoupon;
import store.aurora.service.CouponService;

import java.util.List;

@Controller
@RequestMapping("/coupon")
public class UserCouponController {

    @Autowired
    private final CouponService couponService;

    public UserCouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    //사용자 쿠폰 환불시
    @PutMapping(value = "/refund/")
    public ResponseEntity<String> userCouponRefund(@RequestBody List<Long> userCouponId){
        if (userCouponId == null || userCouponId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User coupon list must not be empty.");
        }

        couponService.refund(userCouponId);

        return ResponseEntity.ok("Coupon refunded successfully.");
    }

    //사용자 쿠폰 사용시
    @PutMapping(value = "/using/")
    public ResponseEntity<String> userCouponUsing(@RequestBody List<Long> userCouponId){
        if (userCouponId == null || userCouponId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User coupon list must not be empty.");
        }

        couponService.used(userCouponId);

        return ResponseEntity.ok("Coupon used successfully.");
    }
}
