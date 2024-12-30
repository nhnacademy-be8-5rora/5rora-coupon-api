package store.aurora.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.aurora.service.CouponService;

import java.util.List;

@RestController
@RequestMapping("/api/coupon")
@RequiredArgsConstructor
public class UserCouponController {

    private final CouponService couponService;

    //사용자 쿠폰 환불시 해당 사용자 쿠폰 상태 변경 및 데이터베이스 동기화
    @PutMapping(value = "/refund")
    public ResponseEntity<String> userCouponRefund(@RequestBody @Valid List<Long> userCouponId){

        couponService.refund(userCouponId);

        return ResponseEntity.ok("User Coupon refunded successfully.");
    }

    //사용자 쿠폰 사용시 해당 사용자 쿠폰 상태 변경 및 데이터베이스 동기화
    @PutMapping(value = "/using/")
    public ResponseEntity<String> userCouponUsing(@RequestBody @Valid List<Long> userCouponId){

        couponService.used(userCouponId);

        return ResponseEntity.ok("User Coupon used successfully.");
    }
}
