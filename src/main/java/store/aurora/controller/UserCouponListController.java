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

    //결제창에서 상품마다 사용가능 쿠폰 리스트 확인(매 상품마다 사용 가능한 쿠폰이 뜨게 해야 됨.
    @GetMapping(value = "/couponList/{userId}")
    public ResponseEntity<List<UserCoupon>> proCouponList(@PathVariable @NotNull Long userId,
                                                          @RequestBody List<String> orderId) {

        //orderId에 있는 카테고리, 북 ID을 불러와서 해당 사용자 쿠폰의 쿠폰정책과 비교해서 쓸 있는지 없는지 확인
        List<UserCoupon> userCoupons = couponListService.getCouponList(userId);


        return null;
    }     //일단 킵, @RequestBody 을 받을 객체를 몰라서

}
