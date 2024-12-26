package store.aurora.handler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import store.aurora.config.GlobalExceptionHandler;
import store.aurora.controller.UserCouponController;
import store.aurora.dto.ProductInfoDTO;
import store.aurora.service.CouponService;

@WebMvcTest(controllers = {UserCouponController.class, GlobalExceptionHandler.class})  // @RestControllerAdvice를 포함한 Spring MVC 테스트
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;  // MockMvc를 사용하여 HTTP 요청 보내기

    @MockBean
    private CouponService couponService;  // MockBean으로 CouponService 제공

    @Autowired
    private ObjectMapper objectMapper;  // JSON 변환을 위한 ObjectMapper

    @Test
    void whenInvalidDto_thenReturnValidationError() throws Exception {
        // 잘못된 DTO 생성 (categoryIds는 null이어야 하지만, 실제로 빈 리스트를 보냄)
        ProductInfoDTO invalidProduct = new ProductInfoDTO(null, null, 1L, 1000);

        // 잘못된 요청 보내기
        mockMvc.perform(put("/coupon/refund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProduct)))  // 잘못된 DTO 전달
                .andExpect(status().isBadRequest())  // HTTP 상태 코드 400 (Bad Request)
                .andExpect(content().string("categoryIds: must not be null"));  // 오류 메시지 검증
    }
}
