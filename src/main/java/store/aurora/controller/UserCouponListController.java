package store.aurora.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.aurora.domain.UserCoupon;
import store.aurora.service.CouponListService;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserCouponListController {

    private final CouponListService couponListService;
    //사용자쿠폰 목록 확인
    @GetMapping(value = "/couponList/{userId}") //->외부 api로 보내는 링크
    public ResponseEntity<List<UserCoupon>> couponList(@PathVariable @NotNull Long userId) {

        List<UserCoupon> userCoupons = couponListService.getCouponList(userId);

        return ResponseEntity.ok(userCoupons);
    }
}
