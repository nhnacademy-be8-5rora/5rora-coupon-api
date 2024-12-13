package store.aurora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import store.aurora.entity.UserCoupon;
import store.aurora.service.CouponListService;

import java.util.List;

@RestController
@RequestMapping("/couponList")
public class UserCouponListController {

    @Autowired
    private final CouponListService couponListService;

    public UserCouponListController(CouponListService couponListService) {
        this.couponListService = couponListService;
    }

    //사용자쿠폰 목록 확인
    @GetMapping(value = "/couponList/{userId}")
    public ResponseEntity<List<UserCoupon>> couponList(@PathVariable Long userId) {
        if(userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<UserCoupon> userCoupons = couponListService.getCouponList(userId);

        return ResponseEntity.ok(userCoupons);
    }

    //결제창에서 상품마다 사용가능 쿠폰 리스트 확인(매 상품마다 사용 가능한 쿠폰이 뜨게 해야 됨.


}
