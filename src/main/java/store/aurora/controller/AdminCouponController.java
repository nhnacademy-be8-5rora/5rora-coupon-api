package store.aurora.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import store.aurora.dto.CouponRequestDto;
import store.aurora.service.CouponPolicyService;

//관리자용 쿠폰 생성 및 배포용
@RestController
@RequestMapping("/admin")
public class AdminCouponController {

    @Autowired
    private final CouponPolicyService couponPolicyService;

    public AdminCouponController(CouponPolicyService couponPolicyService) {
        this.couponPolicyService = couponPolicyService;
    }

//    //todo 쿠폰 정책 생성 폼을 제시
//    @GetMapping("/coupon-policy")
//    public String showCouponPolicyForm(Model model) {
//        model.addAttribute("couponPolicy", new CouponPolicy());
//        return ResponseEntity.ok().body("쿠폰정보가 생성되었습니다."); // 쿠폰 정책 폼 뷰
//    }

//    //todo 관리자가 입력한 쿠폰 정책을 생성후 데이터베이스에 동기화
//    @PostMapping("/coupon-policy")
//    public ResponseEntity<String> createCouponPolicy(@ModelAttribute CouponPolicy couponPolicy) {
//        couponPolicyService.createCouponPolicy(couponPolicy);
//        return ResponseEntity.ok().body("쿠폰정보가 생성되었습니다."); // 쿠폰 정책 목록 페이지로 리디렉션
//    }
//
////    //todo 특정 유저에게 특정 쿠폰정책을 가진 쿠폰을 주는 폼
////    @GetMapping("/coupon-distribution")
////    public String giveCouponToUserForm() {
////        return "admin/coupon-policy-list";
////    }
//
//    //todo 관리자가 입력한 쿠폰을 주는 명령어(특정 한명에게 줄 수 있으며, 특정 조건을 충족한 유저들에게 쿠폰을 뿌릴 수 있도록 함)
//    @PostMapping("/coupon-distribution")
//    public String giveCouponToUser() {
//        return "admin/coupon-policy-list";
//    }

// BindingResult 검증을 처리하는 메서드
private ResponseEntity<String> handleValidationErrors(BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
        String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
    return null; // 에러가 없으면 null 반환
}

    // 쿠폰정책 생성 (관리자) -> 쿠폰 정책은 생성밖에 안됨(정책 수정, 삭제시 -> 그 전에 이미 그 쿠폰을 가진 유저들이 피해를 볼 수 있음)
    @PostMapping(value = "/coupon/create")
    public ResponseEntity<String> couponPolicyCreate(@RequestBody @Valid CouponRequestDto couponRequestDto,
                                               BindingResult bindingResult) { //@Valid 유효 검증

        // 유효성 검사(BindingResult -> 폼 데이터 검증후 오류 데이터 정보 담음
        ResponseEntity<String> errorResponse = handleValidationErrors(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        couponPolicyService.couponPolicyCreate(couponRequestDto);  // 실제 쿠폰 생성 처리

        return ResponseEntity.ok("쿠폰정보가 생성되었습니다.");
    }

    // 사용자쿠폰 수정 (관리자)
    @PutMapping(value = "/coupon/update/")
    public ResponseEntity<String> couponUpdate(@RequestBody @Valid CouponRequestDto couponRequestDto,
                                               BindingResult bindingResult) {
        // 유효성 검사
        ResponseEntity<String> errorResponse = handleValidationErrors(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        couponPolicyService.couponUpdate(couponRequestDto);  // 실제 쿠폰 수정 처리
        return ResponseEntity.ok("사용자쿠폰이 수정되었습니다.");
    }

//    // 사용자쿠폰 삭제 (관리자/자동)
//    @DeleteMapping(value = "/coupon/delete/{userCouponId}")
//    public ResponseEntity<String> couponDelete(@PathVariable("userCouponId") String couponId) {
//        couponPolicyService.couponDelete(couponId);  // 실제 쿠폰 삭제 처리
//        return ResponseEntity.ok("사용자쿠폰이 삭제되었습니다.");
//    }

    //todo 관리자가 입력한 쿠폰을 주는 명령어(특정 한명에게 줄 수 있으며, 특정 조건을 충족한 유저들에게 쿠폰을 뿌릴 수 있도록 함)
    @PostMapping("/coupon/distribution")
    public ResponseEntity<String> couponCreate(@RequestBody @Valid CouponRequestDto couponRequestDto,
                                                   BindingResult bindingResult) {

        ResponseEntity<String> errorResponse = handleValidationErrors(bindingResult);
        if (errorResponse != null) {
            return errorResponse;
        }

        couponPolicyService.userCouponCreate(couponRequestDto);

        return ResponseEntity.ok("사용자쿠폰이 생성되었습니다.");
    }

}
