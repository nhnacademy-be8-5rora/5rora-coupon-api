package store.aurora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import store.aurora.entity.CouponPolicy;
import store.aurora.service.CouponPolicyService;

@Controller
@RequestMapping("/admin")
public class MakeCouponPolicyController {
    @Autowired
    private CouponPolicyService couponPolicyService;

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
}
