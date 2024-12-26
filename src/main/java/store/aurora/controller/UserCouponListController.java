package store.aurora.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import store.aurora.domain.UserCoupon;
import store.aurora.dto.ProductInfoDTO;
import store.aurora.service.CouponListService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserCouponListController {

    private final CouponListService couponListService;

    //사용자쿠폰 목록 확인
    @PostMapping(value = "/couponList/{userId}") //->외부 api로 보내는 링크
    public ResponseEntity<List<UserCoupon>> couponList(@PathVariable @Valid Long userId) {

        List<UserCoupon> userCoupons = couponListService.getCouponList(userId);

        return ResponseEntity.ok(userCoupons);
    }

    //결제창에서 상품마다 사용가능 쿠폰 리스트 확인(매 상품마다 사용 가능한 쿠폰이 뜨게 해야 됨.
    @PostMapping(value = "/calculate/{userId}")
    public ResponseEntity<Map<Long, List<UserCoupon>>> proCouponList(@PathVariable Long userId,
                                                          @RequestBody @Validated List<ProductInfoDTO> productInfoDTO) {   //결제 API에서 필요한 값을 받아야함

        //orderId에 있는 카테고리, 북 ID을 불러와서 해당 사용자 쿠폰의 쿠폰정책과 비교해서 쓸 있는지 없는지 확인
        Map<Long, List<UserCoupon>> userCoupons = couponListService.getCouponListByCategory(productInfoDTO, userId);

        return ResponseEntity.ok(userCoupons);
    }
}
