package store.aurora.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import store.aurora.dto.RequestUserCouponDTO;
import store.aurora.service.AdminCouponService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@WebMvcTest(WelcomeCouponController.class)
class WelcomeCouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminCouponService adminCouponService;

    private Long userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = 1L;  // 예시 유저 ID
        RequestUserCouponDTO requestUserCouponDTO = new RequestUserCouponDTO();
        requestUserCouponDTO.setUserId(List.of(userId));
        requestUserCouponDTO.setCouponPolicyId(1L); // 정책 ID
        requestUserCouponDTO.setStartDate(LocalDate.now());
        requestUserCouponDTO.setEndDate(LocalDate.now().plusDays(30));
    }

    @Test
    void testRegisterUser_AlreadyHasCoupon() throws Exception {
        // given: 이미 쿠폰이 발급된 사용자
        when(adminCouponService.existWelcomeCoupon(userId, 1L)).thenReturn(true);

        // when & then: API 요청 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.post("/welcomeCoupon")
                        .contentType("application/json")
                        .content(String.valueOf(userId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("이미 Welcome 쿠폰이 발급되었습니다."));

        // verify: adminCouponService가 한 번만 호출되었는지 검증
        verify(adminCouponService, times(1)).existWelcomeCoupon(userId, 1L);
    }

    @Test
    void testRegisterUser_CouponCreatedSuccessfully() throws Exception {
        // given: 쿠폰을 발급받지 않은 사용자
        when(adminCouponService.existWelcomeCoupon(userId, 1L)).thenReturn(false);
        when(adminCouponService.userCouponCreate(any(RequestUserCouponDTO.class))).thenReturn(true);

        // when & then: API 요청 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.post("/welcomeCoupon")
                        .contentType("application/json")
                        .content(String.valueOf(userId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("회원가입 성공! Welcome 쿠폰 발급 요청이 처리되었습니다."));

        // verify: adminCouponService가 두 번 호출되었는지 검증
        verify(adminCouponService, times(1)).existWelcomeCoupon(userId, 1L);
        verify(adminCouponService, times(1)).userCouponCreate(any(RequestUserCouponDTO.class));
    }

    @Test
    void testRegisterUser_CouponCreationFailed() throws Exception {
        // given: 쿠폰을 발급받지 않은 사용자
        when(adminCouponService.existWelcomeCoupon(userId, 1L)).thenReturn(false);
        when(adminCouponService.userCouponCreate(any(RequestUserCouponDTO.class))).thenReturn(false);

        // when & then: API 요청 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.post("/welcomeCoupon")
                        .contentType("application/json")
                        .content(String.valueOf(userId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Welcome 쿠폰 발급 요청이 실패되었습니다. 재발급 버튼을 눌러주세요."));

        // verify: adminCouponService가 두 번 호출되었는지 검증
        verify(adminCouponService, times(1)).existWelcomeCoupon(userId, 1L);
        verify(adminCouponService, times(1)).userCouponCreate(any(RequestUserCouponDTO.class));
    }
}
