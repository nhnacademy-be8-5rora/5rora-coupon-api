package store.aurora.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import store.aurora.domain.SaleType;
import store.aurora.dto.AddPolicyDTO;
import store.aurora.dto.DiscountRuleDTO;
import store.aurora.dto.RequestCouponPolicyDTO;
import store.aurora.dto.RequestUserCouponDTO;
import store.aurora.service.AdminCouponService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminCouponController.class)
class AdminCouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminCouponService adminCouponService;

    //쿠폰 정책 생성 테스트
    @Test
    void testCouponPolicyCreate() throws Exception {
        DiscountRuleDTO discountRuleDTO = new DiscountRuleDTO();
        discountRuleDTO.setSalePercent(10);
        discountRuleDTO.setSaleAmount(null);

        AddPolicyDTO addPolicyDTO = new AddPolicyDTO();
        addPolicyDTO.setCategoryId(List.of(1L));
        addPolicyDTO.setBookId(List.of(1L));

        RequestCouponPolicyDTO requestCouponPolicyDTO = new RequestCouponPolicyDTO();
        requestCouponPolicyDTO.setPolicyName("testPolicy");
        requestCouponPolicyDTO.setSaleType(SaleType.PERCENT);
        requestCouponPolicyDTO.setDiscountRuleDTO(discountRuleDTO);
        requestCouponPolicyDTO.setAddPolicyDTO(addPolicyDTO);

        doNothing().when(adminCouponService).couponPolicyCreate(Mockito.any(), Mockito.any(), Mockito.any());

        // Act
        var result = mockMvc.perform(post("/admin/coupon/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCouponPolicyDTO)))
                .andReturn();

        // Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(200); // 상태 코드 검증
        assertThat(result.getResponse().getContentAsString()).isEqualTo("쿠폰정보가 생성되었습니다."); // 응답 본문 검증
    }


    // 사용자 쿠폰 생성 테스트
    @Test
    void testUserCouponCreate_Success() throws Exception {
        RequestUserCouponDTO requestDto = new RequestUserCouponDTO();
        requestDto.setUserId(List.of(1L));
        requestDto.setCouponPolicyId(2L);

        doNothing().when(adminCouponService).userCouponCreate(Mockito.any());

        var result = mockMvc.perform(post("/admin/coupon/distribution")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).isEqualTo("사용자쿠폰이 생성되었습니다.");
    }
}
