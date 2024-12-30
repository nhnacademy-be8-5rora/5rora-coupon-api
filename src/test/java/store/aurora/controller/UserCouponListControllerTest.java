package store.aurora.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import store.aurora.domain.*;
import store.aurora.dto.ProductInfoDTO;
import store.aurora.repository.*;
import store.aurora.service.CouponListService;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc  // MockMvc 설정 자동
@Transactional
class UserCouponListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponListService couponListService;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    @Autowired
    private DisCountRuleRepository discountRuleRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookPolicyRepository bookPolicyRepository;
    @Autowired
    private CategoryPolicyRepository categoryPolicyRepository;

    @BeforeEach
    void setUp() {
        // @BeforeAll은 static으로 선언되어야 하지만, @TestInstance(PER_CLASS)로 인스턴스 메서드처럼 사용할 수 있음

        // discountRule 설정
        DiscountRule discountRule = new DiscountRule();
        discountRule.setSaleAmount(10000);
        discountRule.setNeedCost(20000);
        discountRule = discountRuleRepository.save(discountRule);

        // CouponPolicy 설정
        CouponPolicy couponPolicy = new CouponPolicy();
        couponPolicy.setName("Test Policy");
        couponPolicy.setSaleType(SaleType.AMOUNT);  // SaleType 설정

        couponPolicy.setDiscountRule(discountRule);

        couponPolicy = couponPolicyRepository.save(couponPolicy);

        BookPolicy bookPolicy = new BookPolicy();
        bookPolicy.setBookId(1L);
        bookPolicy.setPolicy(couponPolicy);

        bookPolicyRepository.save(bookPolicy);

        CategoryPolicy categoryPolicy = new CategoryPolicy();
        categoryPolicy.setCategoryId(1L);
        categoryPolicy.setPolicy(couponPolicy);

        categoryPolicyRepository.save(categoryPolicy);

        // UserCoupon 데이터 추가
        UserCoupon coupon1 = new UserCoupon();
        coupon1.setUserId("asdf");  // userId 설정
        coupon1.setStartDate(LocalDate.now().minusDays(10));  // startDate 설정
        coupon1.setEndDate(LocalDate.now().minusDays(1));  // 만료된 상태
        coupon1.setPolicy(couponPolicy);  // CouponPolicy 설정

        UserCoupon coupon2 = new UserCoupon();
        coupon2.setUserId("asdf2");  // userId 설정
        coupon2.setStartDate(LocalDate.now().minusDays(10));  // startDate 설정
        coupon2.setEndDate(LocalDate.now().plusDays(5));  // 아직 만료되지 않음
        coupon2.setPolicy(couponPolicy);  // CouponPolicy 설정

        userCouponRepository.save(coupon1);
        userCouponRepository.save(coupon2);
    }

    // 사용자 쿠폰 목록 조회 테스트
    @Test
    void testCouponList() throws Exception {

        String userId = "asdf";

        List<UserCoupon> userCoupon = userCouponRepository.findByUserId(userId);

        Mockito.when(couponListService.getCouponList(userId))
                .thenReturn(userCoupon);

        // When
        var result = mockMvc.perform(post("/couponList/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // 상태 코드 검증
                .andReturn();

        // Then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);  // 상태 코드 검증
        assertThat(result.getResponse().getContentAsString()).contains("Test Policy");  // 응답 본문 검증

        verify(couponListService, times(1)).getCouponList(userId);  // 서비스 메서드 호출 횟수 검증
        verifyNoMoreInteractions(couponListService);  // 그 외의 상호작용이 없음을 검증
    }

    // 상품별 사용 가능한 쿠폰 목록 조회 테스트
    @Test
    void testProCouponList() throws Exception {

        String userId = "asdf";

        List<UserCoupon> userCoupon = userCouponRepository.findByUserId(userId);

        List<ProductInfoDTO> productInfoDTOList = new ArrayList<>();
        productInfoDTOList.add(new ProductInfoDTO(1L, List.of(1L), 1L, 1000));
        productInfoDTOList.add(new ProductInfoDTO(2L, List.of(2L), 2L, 1500));

        Mockito.when(couponListService.getCouponListByCategory(productInfoDTOList, userId))
                .thenReturn(Map.of(1L, userCoupon));

        // When
        var result = mockMvc.perform(post("/calculate/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productInfoDTOList)))
                .andExpect(status().isOk())  // 상태 코드 검증
                .andReturn();

        // Then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);  // 상태 코드 검증
        assertThat(result.getResponse().getContentAsString()).contains("Test Policy");  // 응답 본문 검증

        verify(couponListService, times(1)).getCouponListByCategory(productInfoDTOList, userId);  // 서비스 메서드 호출 횟수 검증
        verifyNoMoreInteractions(couponListService);  // 그 외의 상호작용이 없음을 검증
    }
}