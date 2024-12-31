package store.aurora.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import store.aurora.domain.CouponState;
import store.aurora.domain.SaleType;
import store.aurora.dto.*;
import store.aurora.service.AdminCouponService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
        requestDto.setUserId(List.of("1L"));
        requestDto.setCouponPolicyId(2L);

        when(adminCouponService.userCouponCreate(Mockito.any())).thenReturn(true);

        var result = mockMvc.perform(post("/admin/coupon/distribution")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).isEqualTo("사용자쿠폰이 생성되었습니다.");
    }

    // 사용자 쿠폰 수정 테스트
    @Test
    void testUserCouponUpdate() throws Exception {
        // Given
        UpdateUserCouponByUserIdDto dto = new UpdateUserCouponByUserIdDto();
        dto.setState(CouponState.USED);  // 쿠폰 상태 변경
        dto.setPolicyId(1L);  // 쿠폰 정책 ID
        dto.setEndDate(LocalDate.now().plusDays(10));  // 만료일 설정
        dto.setUserIds(Arrays.asList("1L", "2L"));  // 수정할 사용자 ID 리스트

        // When
        var result = mockMvc.perform(put("/admin/coupon/update/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())  // 응답 상태가 200 OK인지 확인
                .andReturn();

        // Then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);  // 상태 코드 검증
        assertThat(result.getResponse().getContentAsString()).isEqualTo("사용자쿠폰이 수정되었습니다.");  // 응답 본문 검증

        // Verify that the coupon update logic was triggered
        verify(adminCouponService, times(1)).couponUpdate(dto);  // couponUpdate 메서드 호출 횟수 검증
        verifyNoMoreInteractions(adminCouponService);  // 그 외에 다른 상호작용이 없는지 확인
    }
}
