package store.aurora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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


    //사용자 쿠폰 사용시
}
