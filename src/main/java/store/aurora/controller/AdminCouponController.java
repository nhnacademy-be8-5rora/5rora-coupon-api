package store.aurora.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import store.aurora.dto.AddPolicyDTO;
import store.aurora.dto.DiscountRuleDTO;
import store.aurora.dto.RequestCouponDto;
import store.aurora.dto.RequestCouponPolicyDTO;
import store.aurora.service.CouponPolicyService;

//관리자용 쿠폰 생성 및 배포용
//관리자를 통해서 새 쿠폰을 데이터베이스에 저장하기 때문에 유효성, 무결성 중요(@Validated 사용)
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminCouponController {

    private final CouponPolicyService couponPolicyService;

    // 쿠폰정책 생성 (관리자) -> 쿠폰 정책은 생성밖에 안됨(정책 수정, 삭제시 -> 이전에 해당 쿠폰을 가진 유저들이 피해를 볼 수 있음)
    //모든 사용자 쿠폰을 확인해서 해당 쿠폰 정책 ID가 있는지 파악한 후에 삭제, 수정 가능하도록 구현은 가능
    @PostMapping(value = "/coupon/create")
    public ResponseEntity<String> couponPolicyCreate(@RequestBody @Validated RequestCouponPolicyDTO requestCouponPolicyDTO) { //@Validated 유효 검증(무결성)

        DiscountRuleDTO discountRuleDTO = requestCouponPolicyDTO.getDiscountRuleDTO();
        AddPolicyDTO addPolicyDTO = requestCouponPolicyDTO.getAddPolicyDTO();

        couponPolicyService.couponPolicyCreate(requestCouponPolicyDTO, discountRuleDTO, addPolicyDTO);  // 실제 쿠폰 생성 처리

        return ResponseEntity.ok("쿠폰정보가 생성되었습니다.");
    }

    // 사용자쿠폰 수정 (관리자)
    @PutMapping(value = "/coupon/update/")
    public ResponseEntity<String> couponUpdate(@RequestBody @Validated RequestCouponDto requestCouponDto) {

        couponPolicyService.couponUpdate(requestCouponDto);  // 실제 쿠폰 수정 처리
        return ResponseEntity.ok("사용자쿠폰이 수정되었습니다.");
    }

    //관리자가 입력한 쿠폰을 주는 명령어(특정 한명에게 줄 수 있으며, 특정 조건을 충족한 유저들에게 쿠폰을 뿌릴 수 있도록 함)
    @PostMapping("/coupon/distribution")
    public ResponseEntity<String> couponCreate(@RequestBody @Validated RequestCouponDto requestCouponDto) {

        couponPolicyService.userCouponCreate(requestCouponDto);

        return ResponseEntity.ok("사용자쿠폰이 생성되었습니다.");
    }

}
