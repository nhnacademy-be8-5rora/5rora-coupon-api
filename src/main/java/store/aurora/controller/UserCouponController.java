package store.aurora.controller;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.aurora.service.CouponService;

import java.util.List;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class UserCouponController {

    private final CouponService couponService;

    //사용자 쿠폰 환불시 해당 사용자 쿠폰 상태 변경 및 데이터베이스 동기화
    @PutMapping(value = "/refund/")
    public ResponseEntity<String> userCouponRefund(@RequestBody @NotEmpty List<Long> userCouponId){

        couponService.refund(userCouponId);

        return ResponseEntity.ok("User Coupon refunded successfully.");
    }

    //사용자 쿠폰 사용시 해당 사용자 쿠폰 상태 변경 및 데이터베이스 동기화
    @PutMapping(value = "/using/")
    public ResponseEntity<String> userCouponUsing(@RequestBody @NotEmpty List<Long> userCouponId){

        couponService.used(userCouponId);

        return ResponseEntity.ok("User Coupon used successfully.");
    }
}
