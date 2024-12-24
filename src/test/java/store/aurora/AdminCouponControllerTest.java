package store.aurora;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import store.aurora.controller.AdminCouponController;
import store.aurora.domain.SaleType;
import store.aurora.dto.AddPolicyDTO;
import store.aurora.dto.DiscountRuleDTO;
import store.aurora.dto.RequestCouponPolicyDTO;
import store.aurora.service.AdminCouponService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(AdminCouponController.class)
class AdminCouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminCouponService adminCouponService;

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
}
