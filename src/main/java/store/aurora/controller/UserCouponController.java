package store.aurora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import store.aurora.entity.UserCoupon;
import store.aurora.service.CouponService;

import java.util.List;

@RestController
@RequestMapping("/coupon")
public class UserCouponController {

    @Autowired
    private final CouponService couponService;

    public UserCouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    //사용자 쿠폰 환불시 해당 사용자 쿠폰 상태 변경 및 데이터베이스 동기화
    @PutMapping(value = "/refund/")
    public ResponseEntity<String> userCouponRefund(@RequestBody List<Long> userCouponId){
        if (userCouponId == null || userCouponId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User coupon list must not be empty.");
        }

        couponService.refund(userCouponId);

        return ResponseEntity.ok("User Coupon refunded successfully.");
    }

    //사용자 쿠폰 사용시 해당 사용자 쿠폰 상태 변경 및 데이터베이스 동기화
    @PutMapping(value = "/using/")
    public ResponseEntity<String> userCouponUsing(@RequestBody List<Long> userCouponId){
        if (userCouponId == null || userCouponId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User coupon list must not be empty.");
        }

        couponService.used(userCouponId);

        return ResponseEntity.ok("User Coupon used successfully.");
    }
}
