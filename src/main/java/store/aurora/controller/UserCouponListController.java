package store.aurora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.aurora.service.CouponListService;

@RestController
@RequestMapping("/couponList")
public class UserCouponListController {

    @Autowired
    private final CouponListService couponListService;

    public UserCouponListController(CouponListService couponListService) {
        this.couponListService = couponListService;
    }

    //사용자쿠폰 목록 확인
    @PutMapping(value = "/couponList/{userId}")
    public ResponseEntity<String> couponList(@PathVariable Long userId) {
        if(userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        couponListService.showCouponList(userId);

        return ResponseEntity.ok(userId + "의 사용자 쿠폰 목록 조회되었습니다.");
    }

    //결제창에서 상품마다 사용가능 쿠폰 리스트 확인(매 상품마다 사용 가능한 쿠폰이 뜨게 해야 됨.


}
