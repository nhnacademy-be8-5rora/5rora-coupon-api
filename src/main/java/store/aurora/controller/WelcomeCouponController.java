package store.aurora.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.aurora.dto.RequestUserCouponDTO;
import store.aurora.service.AdminCouponService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class WelcomeCouponController {

    private final AdminCouponService adminCouponService;

    // 회원가입 API
    @PostMapping("/register")
    public String registerUser(@RequestBody Long userId) {
        LocalDate currentDate = LocalDate.now();

        RequestUserCouponDTO requestUserCouponDTO = new RequestUserCouponDTO();
        requestUserCouponDTO.setUserId(List.of(userId));
        requestUserCouponDTO.setCouponPolicyId(1L); //사용자 환영 쿠폰 정책
        requestUserCouponDTO.setStartDate(currentDate);
        requestUserCouponDTO.setStartDate(currentDate.plusDays(30));

        boolean success = adminCouponService.userCouponCreate(requestUserCouponDTO);

        if (success) {
            return "회원가입 성공! Welcome 쿠폰 발급 요청이 처리되었습니다.";
        }


        return "Welcome 쿠폰 발급 요청이 실패되었습니다. 재발급 버튼을 눌러주세요.";
    }
}
