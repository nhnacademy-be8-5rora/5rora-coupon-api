package store.aurora.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import store.aurora.domain.*;
import store.aurora.dto.*;
import store.aurora.repository.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class AdminCouponServiceTest {

    @Autowired private AdminCouponService adminCouponService;
    @MockBean private CouponPolicyRepository couponPolicyRepository;
    @MockBean private CouponRepository couponRepository;
    @MockBean private DisCountRuleRepository disCountRuleRepository;
    @MockBean private CategoryPolicyRepository categoryPolicyRepository;
    @MockBean private BookPolicyRepository bookPolicyRepository;

    @Captor
    private ArgumentCaptor<List<Long>> userIdsCaptor;
    @Captor
    private ArgumentCaptor<List<UserCoupon>> userCouponCaptor;
    @Captor
    private ArgumentCaptor<CouponPolicy> couponPolicyCaptor;
    @Captor
    private ArgumentCaptor<DiscountRule> discountRuleCaptor;
    @Captor
    private ArgumentCaptor<List<CategoryPolicy>> categoryPolicyCaptor;
    @Captor
    private ArgumentCaptor<List<BookPolicy>> bookPolicyCaptor;



    @Test
    @DisplayName("쿠폰 상태만 변경 테스트")
    void testCouponUpdate_ChangeCouponStateOnly() {
        // given
        UpdateUserCouponByUserIdDto dto = new UpdateUserCouponByUserIdDto();
        dto.setState(CouponState.LIVE);
        dto.setUserIds(List.of(1L, 2L, 3L));

        ArgumentCaptor<CouponState> stateCaptor = ArgumentCaptor.forClass(CouponState.class);

        // when
        adminCouponService.couponUpdate(dto);

        verify(couponRepository).updateCouponStateByUserIds(stateCaptor.capture(), userIdsCaptor.capture());

        assertThat(stateCaptor.getValue()).isEqualTo(CouponState.LIVE);
        assertThat(userIdsCaptor.getValue()).containsExactly(1L, 2L, 3L);
    }

    @Test
    @DisplayName("쿠폰 정책 ID만 변경 테스트")
    void testCouponUpdate_ChangePolicyIdOnly() {
        // given
        UpdateUserCouponByUserIdDto dto = new UpdateUserCouponByUserIdDto();
        dto.setPolicyId(10L);
        dto.setUserIds(List.of(4L, 5L));

        ArgumentCaptor<Long> policyIdCaptor = ArgumentCaptor.forClass(Long.class);

        // when
        adminCouponService.couponUpdate(dto);

        // then
        verify(couponRepository).updateCouponPolicyByUserIds(policyIdCaptor.capture(), userIdsCaptor.capture());

        assertThat(policyIdCaptor.getValue()).isEqualTo(10L);
        assertThat(userIdsCaptor.getValue()).containsExactly(4L, 5L);
    }

    @Test
    @DisplayName("쿠폰 종료 날짜만 변경 테스트")
    void testCouponUpdate_ChangeEndDateOnly() {
        // given
        UpdateUserCouponByUserIdDto dto = new UpdateUserCouponByUserIdDto();
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        dto.setEndDate(endDate);
        dto.setUserIds(List.of(6L, 7L));

        ArgumentCaptor<LocalDate> endDateCaptor = ArgumentCaptor.forClass(LocalDate.class);

        // when
        adminCouponService.couponUpdate(dto);

        // then
        verify(couponRepository).updateCouponEndDateByUserIds(endDateCaptor.capture(), userIdsCaptor.capture());

        assertThat(endDateCaptor.getValue()).isEqualTo(endDate);
        assertThat(userIdsCaptor.getValue()).containsExactly(6L, 7L);
    }

    @Test
    @DisplayName("모든 필드 변경 테스트")
    void testCouponUpdate_ChangeAllFields() {
        // given
        UpdateUserCouponByUserIdDto dto = new UpdateUserCouponByUserIdDto();
        dto.setState(CouponState.TIMEOUT);
        dto.setPolicyId(20L);
        LocalDate endDate = LocalDate.of(2026, 1, 1);
        dto.setEndDate(endDate);
        dto.setUserIds(List.of(8L, 9L));

        ArgumentCaptor<CouponState> stateCaptor = ArgumentCaptor.forClass(CouponState.class);
        ArgumentCaptor<Long> policyIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<LocalDate> endDateCaptor = ArgumentCaptor.forClass(LocalDate.class);

        // when
        adminCouponService.couponUpdate(dto);

        // then
        // 상태 변경 검증
        verify(couponRepository).updateCouponStateByUserIds(stateCaptor.capture(), userIdsCaptor.capture());
        assertThat(stateCaptor.getValue()).isEqualTo(CouponState.TIMEOUT);
        assertThat(userIdsCaptor.getValue()).containsExactly(8L, 9L);

        // 정책 ID 변경 검증
        verify(couponRepository).updateCouponPolicyByUserIds(policyIdCaptor.capture(), userIdsCaptor.capture());
        assertThat(policyIdCaptor.getValue()).isEqualTo(20L);
        assertThat(userIdsCaptor.getValue()).containsExactly(8L, 9L);

        // 종료 날짜 변경 검증
        verify(couponRepository).updateCouponEndDateByUserIds(endDateCaptor.capture(), userIdsCaptor.capture());
        assertThat(endDateCaptor.getValue()).isEqualTo(endDate);
        assertThat(userIdsCaptor.getValue()).containsExactly(8L, 9L);
    }

    @Test
    @DisplayName("사용자 쿠폰 생성 성공 테스트")
    void testUserCouponCreate_Success() {
        // given
        Long policyId = 1L;
        CouponState state = CouponState.LIVE;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(30);
        List<Long> userIds = List.of(1L, 2L, 3L);

        CouponPolicy couponPolicy = new CouponPolicy();
        couponPolicy.setId(policyId);  // 정책 설정

        // mock
        when(couponPolicyRepository.findById(policyId)).thenReturn(Optional.of(couponPolicy));

        RequestUserCouponDTO requestDto = new RequestUserCouponDTO();
        requestDto.setUserId(userIds);
        requestDto.setCouponPolicyId(policyId);
        requestDto.setState(state);
        requestDto.setStartDate(startDate);
        requestDto.setEndDate(endDate);

        // when
        boolean result = adminCouponService.userCouponCreate(requestDto);

        // then
        assertThat(result).isTrue();
        verify(couponRepository).saveAll(userCouponCaptor.capture());
        List<UserCoupon> capturedCoupons = userCouponCaptor.getValue();

        // UserCoupon이 3개가 생성된 것을 확인
        assertThat(capturedCoupons).hasSize(3);
        assertThat(capturedCoupons.get(0).getUserId()).isEqualTo(1L);
        assertThat(capturedCoupons.get(1).getUserId()).isEqualTo(2L);
        assertThat(capturedCoupons.get(2).getUserId()).isEqualTo(3L);
        assertThat(capturedCoupons.get(0).getPolicy()).isEqualTo(couponPolicy);
        assertThat(capturedCoupons.get(0).getCouponState()).isEqualTo(CouponState.LIVE);
        assertThat(capturedCoupons.get(0).getStartDate()).isEqualTo(startDate);
        assertThat(capturedCoupons.get(0).getEndDate()).isEqualTo(endDate);
    }

    @Test
    @DisplayName("정책이 존재하지 않으면 쿠폰 생성 실패 테스트")
    void testUserCouponCreate_PolicyNotFound() {
        // given
        Long policyId = 1L;
        CouponState state = CouponState.LIVE;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(30);
        List<Long> userIds = List.of(1L, 2L, 3L);

        // mock
        when(couponPolicyRepository.findById(policyId)).thenReturn(Optional.empty());

        RequestUserCouponDTO requestDto = new RequestUserCouponDTO();
        requestDto.setUserId(userIds);
        requestDto.setCouponPolicyId(policyId);
        requestDto.setState(state);
        requestDto.setStartDate(startDate);
        requestDto.setEndDate(endDate);

        // when
        boolean result = adminCouponService.userCouponCreate(requestDto);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("예외 발생 시 쿠폰 생성 실패 테스트")
    void testUserCouponCreate_Exception() {
        // given
        Long policyId = 1L;
        CouponState state = CouponState.LIVE;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(30);
        List<Long> userIds = List.of(1L, 2L, 3L);

        CouponPolicy couponPolicy = new CouponPolicy();
        couponPolicy.setId(policyId);

        // mock
        when(couponPolicyRepository.findById(policyId)).thenReturn(Optional.of(couponPolicy));
        when(couponRepository.saveAll(anyList())).thenThrow(new RuntimeException("Database error"));

        RequestUserCouponDTO requestDto = new RequestUserCouponDTO();
        requestDto.setUserId(userIds);
        requestDto.setCouponPolicyId(policyId);
        requestDto.setState(state);
        requestDto.setStartDate(startDate);
        requestDto.setEndDate(endDate);

        // when
        boolean result = adminCouponService.userCouponCreate(requestDto);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("쿠폰 정책 생성 테스트")
    void testCouponPolicyCreate() {
        // given
        Long categoryId = 1L;
        Long bookId = 2L;
        RequestCouponPolicyDTO requestCouponPolicyDTO = new RequestCouponPolicyDTO();
        requestCouponPolicyDTO.setPolicyName("Test Policy");
        requestCouponPolicyDTO.setSaleType(SaleType.AMOUNT);

        DiscountRuleDTO discountRuleDTO = new DiscountRuleDTO();
        discountRuleDTO.setNeedCost(1000);
        discountRuleDTO.setMaxSale(500);
        discountRuleDTO.setSaleAmount(100);

        AddPolicyDTO addPolicyDTO = new AddPolicyDTO();
        addPolicyDTO.setCategoryId(List.of(categoryId));
        addPolicyDTO.setBookId(List.of(bookId));

        // when
        adminCouponService.couponPolicyCreate(requestCouponPolicyDTO, discountRuleDTO, addPolicyDTO);

        // then
        // 1. discountRuleRepository.save 호출 확인
        verify(disCountRuleRepository).save(discountRuleCaptor.capture());
        DiscountRule capturedDiscountRule = discountRuleCaptor.getValue();
        assertThat(capturedDiscountRule.getNeedCost()).isEqualTo(1000);
        assertThat(capturedDiscountRule.getMaxSale()).isEqualTo(500);
        assertThat(capturedDiscountRule.getSaleAmount()).isEqualTo(100);

        // 2. couponPolicyRepository.save 호출 확인
        verify(couponPolicyRepository).save(couponPolicyCaptor.capture());
        CouponPolicy capturedCouponPolicy = couponPolicyCaptor.getValue();
        assertThat(capturedCouponPolicy.getName()).isEqualTo("Test Policy");
        assertThat(capturedCouponPolicy.getSaleType()).isEqualTo(SaleType.AMOUNT);

        // 3. categoryPolicyRepository.saveAll 호출 확인
        verify(categoryPolicyRepository).saveAll(categoryPolicyCaptor.capture());
        List<CategoryPolicy> capturedCategoryPolicies = categoryPolicyCaptor.getValue();
        assertThat(capturedCategoryPolicies).hasSize(1);
        assertThat(capturedCategoryPolicies.get(0).getCategoryId()).isEqualTo(categoryId);

        // 4. bookPolicyRepository.saveAll 호출 확인
        verify(bookPolicyRepository).saveAll(bookPolicyCaptor.capture());
        List<BookPolicy> capturedBookPolicies = bookPolicyCaptor.getValue();
        assertThat(capturedBookPolicies).hasSize(1);
        assertThat(capturedBookPolicies.get(0).getBookId()).isEqualTo(bookId);
    }

    @Test
    @DisplayName("Welcome 쿠폰 생성 확인 테스트")
    void testExistWelcomeCoupon_WhenCouponDoesNotExist() {
        // Arrange
        Long userId = 1L;
        Long policyId = 1L;

        // Act
        boolean result = adminCouponService.existWelcomeCoupon(userId, policyId);

        // Assert
        assertThat(result)
                .as("Check if the coupon does not exist")
                .isFalse();  // Asserting that the result is false
    }
}
