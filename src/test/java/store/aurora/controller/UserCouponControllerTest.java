package store.aurora.controller;

import static org.mockito.Mockito.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import store.aurora.service.CouponService;


import java.util.Arrays;
import java.util.List;

@WebMvcTest(UserCouponController.class)
class UserCouponControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponService couponService;  // CouponService를 mock 객체로 주입

    @Autowired
    private ObjectMapper objectMapper;  // ObjectMapper 사용

    // 사용자 쿠폰 환불 성공 테스트
    @Test
    void testUserCouponRefund() throws Exception {
        // Given
        List<Long> userCouponIdList = Arrays.asList(1L, 2L, 3L);

        // When
        mockMvc.perform(put("/coupon/refund/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(objectMapper.writeValueAsString(userCouponIdList)))
                .andExpect(status().isOk())  // 상태 코드 200 OK 검증
                .andExpect(content().string("User Coupon refunded successfully."));  // 응답 본문 검증

        // Then
        verify(couponService, times(1)).refund(userCouponIdList);  // refund 메서드가 정확히 한 번 호출되었는지 검증
        verifyNoMoreInteractions(couponService);  // 다른 상호작용이 없음을 검증
    }

    // 사용자 쿠폰 사용 성공 테스트
    @Test
    void testUserCouponUsing() throws Exception {
        // Given
        List<Long> userCouponIdList = Arrays.asList(1L, 2L, 3L);

        // When
        mockMvc.perform(put("/coupon/using/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(objectMapper.writeValueAsString(userCouponIdList)))
                .andExpect(status().isOk())  // 상태 코드 200 OK 검증
                .andExpect(content().string("User Coupon used successfully."));  // 응답 본문 검증

        // Then
        verify(couponService, times(1)).used(userCouponIdList);  // used 메서드가 정확히 한 번 호출되었는지 검증
        verifyNoMoreInteractions(couponService);  // 다른 상호작용이 없음을 검증
    }
}
