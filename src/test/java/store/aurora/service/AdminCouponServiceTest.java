package store.aurora.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import store.aurora.domain.*;
import store.aurora.dto.*;
import store.aurora.repository.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
class AdminCouponServiceTest {

    @Autowired private CouponPolicyRepository couponPolicyRepository;
    @Autowired private CouponRepository couponRepository;
    @Autowired private DisCountRuleRepository disCountRuleRepository;
    @Autowired private CategoryPolicyRepository categoryPolicyRepository;
    @Autowired private BookPolicyRepository bookPolicyRepository;

    @Autowired
    private AdminCouponService adminCouponService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);  // Mock 객체 초기화
    }

    @Test
    void testCouponPolicyCreate() {
        //쿠폰정책 DTO
        RequestCouponPolicyDTO requestCouponPolicyDTO = new RequestCouponPolicyDTO();
        requestCouponPolicyDTO.setPolicyName("Test Policy");
        requestCouponPolicyDTO.setSaleType(SaleType.AMOUNT);
        //계산 DTO
        DiscountRuleDTO discountRuleDTO = new DiscountRuleDTO();
        discountRuleDTO.setSaleAmount(10000);
        //책/카테고리 DTO
        AddPolicyDTO addPolicyDTO = new AddPolicyDTO();
        addPolicyDTO.setCategoryId(Arrays.asList(1L, 2L));
        addPolicyDTO.setBookId(Arrays.asList(3L, 4L));

        // When
        adminCouponService.couponPolicyCreate(requestCouponPolicyDTO, discountRuleDTO, addPolicyDTO);

        // DiscountRule 검증
        ArgumentCaptor<DiscountRule> discountRuleCaptor = ArgumentCaptor.forClass(DiscountRule.class);
        verify(disCountRuleRepository).save(discountRuleCaptor.capture());

        DiscountRule capturedDiscountRule = discountRuleCaptor.getValue();
        assertThat(capturedDiscountRule.getSaleAmount()).isEqualTo(10000);

        // CouponPolicy 검증
        ArgumentCaptor<CouponPolicy> couponPolicyCaptor = ArgumentCaptor.forClass(CouponPolicy.class);
        verify(couponPolicyRepository).save(couponPolicyCaptor.capture());

        CouponPolicy capturedCouponPolicy = couponPolicyCaptor.getValue();
        assertThat(capturedCouponPolicy.getName()).isEqualTo("Test Policy");
        assertThat(capturedCouponPolicy.getSaleType()).isEqualTo(SaleType.AMOUNT);

        // CategoryPolicy와 BookPolicy는 리스트로 검증
        verify(categoryPolicyRepository).saveAll(anyList());
        verify(bookPolicyRepository).saveAll(anyList());
    }

    @Test
    void testCouponUpdate() {
        // 유저 ID을 통해 사용자 쿠폰 변경
        UpdateUserCouponByUserIdDto dto = new UpdateUserCouponByUserIdDto();

        dto.setState(CouponState.USED);
        dto.setPolicyId(1L);
        dto.setEndDate(LocalDate.now().plusDays(10));
        dto.setUserIds(Arrays.asList(1L, 2L));      //받아올 유저 ID

        // 사용자 쿠폰 수정
        adminCouponService.couponUpdate(dto);

        // Assert
        verify(couponRepository, times(1))
                .updateCouponStateByUserIds(CouponState.USED, dto.getUserIds());
        verify(couponRepository, times(1))
                .updateCouponPolicyByUserIds(1L, dto.getUserIds());
        verify(couponRepository, times(1))
                .updateCouponEndDateByUserIds(LocalDate.now().plusDays(10), dto.getUserIds());
        verifyNoMoreInteractions(couponRepository);
    }

    @Test
    void testUserCouponCreate() {
        // Arrange
        Long userId = 1L;
        Long policyId = 1L;
        LocalDate currentDate = LocalDate.now();
        RequestUserCouponDTO requestUserCouponDTO = new RequestUserCouponDTO();
        requestUserCouponDTO.setUserId(List.of(userId));
        requestUserCouponDTO.setCouponPolicyId(policyId);
        requestUserCouponDTO.setStartDate(currentDate);
        requestUserCouponDTO.setEndDate(currentDate.plusDays(30));

        // Act
        boolean result = adminCouponService.userCouponCreate(requestUserCouponDTO);

        // Assert
        assertThat(result).isTrue(); // Verify the result

        // Verify that saveAll was called with the expected argument
        verify(couponRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testExistWelcomeCoupon_WhenCouponDoesNotExist() {
        // Arrange
        Long userId = 1L;
        Long policyId = 1L;
        when(couponRepository.existsByUserIdAndPolicyId(userId, policyId)).thenReturn(false);

        // Act
        boolean result = adminCouponService.existWelcomeCoupon(userId, policyId);

        // Assert
        assertThat(result)
                .as("Check if the coupon does not exist")
                .isFalse();  // Asserting that the result is false
    }
}
