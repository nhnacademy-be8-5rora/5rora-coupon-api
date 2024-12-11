package store.aurora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import store.aurora.entity.CouponPolicy;
import store.aurora.service.CouponPolicyService;

//관리자용 쿠폰 생성 및 배포용
@RestController
@RequestMapping("/admin")
public class MakeCouponPolicyController {

    @Autowired
    private final CouponPolicyService couponPolicyService;

    public MakeCouponPolicyController(CouponPolicyService couponPolicyService) {
        this.couponPolicyService = couponPolicyService;
    }

    //쿠폰 정책 생성 폼을 제시
    @GetMapping("/coupon-policy")
    public String showCouponPolicyForm(Model model) {
        model.addAttribute("couponPolicy", new CouponPolicy());
        return "admin/coupon-policy-form"; // 쿠폰 정책 폼 뷰
    }

    @PostMapping("/coupon-policy")
    public String createCouponPolicy(@ModelAttribute CouponPolicy couponPolicy) {
        couponPolicyService.createCouponPolicy(couponPolicy);
        return "redirect:/admin/coupon-policy-list"; // 쿠폰 정책 목록 페이지로 리디렉션
    }

    //특정 유저에게 쿠폰을 주는 폼
    @GetMapping("/coupon-distribution")
    public String giveCouponToUserForm() {
        return "admin/coupon-policy-list";
    }

    //관리자가 입력한 쿠폰을 주는 명령어(특정 한명에게 줄 수 있으며, 특정 조건을 충족한 유저들에게 쿠폰을 뿌릴 수 있도록 함)
    @PostMapping("/coupon-distribution")
    public String giveCouponToUser() {
        return "admin/coupon-policy-list";
    }

}
