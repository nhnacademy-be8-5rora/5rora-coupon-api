package store.aurora.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import store.aurora.service.CouponService;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(UserCouponController.class)
class UserCouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponService couponService;

    @Test
    void testUserCouponRefund() throws Exception {
        List<Long> userCouponIds = List.of(1L, 2L, 3L);

        // Mock the service behavior
        doNothing().when(couponService).refund(userCouponIds);

        // Test the controller
        mockMvc.perform(put("/coupon/refund/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1, 2, 3]"))
                .andExpect(status().isOk())
                .andExpect(content().string("User Coupon refunded successfully."));
    }

    @Test
    void testUserCouponUsing() throws Exception {
        List<Long> userCouponIds = List.of(4L, 5L, 6L);

        // Mock the service behavior
        doNothing().when(couponService).used(userCouponIds);

        // Test the controller
        mockMvc.perform(put("/coupon/using/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[4, 5, 6]"))
                .andExpect(status().isOk())
                .andExpect(content().string("User Coupon used successfully."));
    }
}
