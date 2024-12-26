package store.aurora.service;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Autowired private AdminCouponService adminCouponService;

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

        // 쿠폰 정책 생성 및 책/카테고리 정책 생성
        adminCouponService.couponPolicyCreate(requestCouponPolicyDTO, discountRuleDTO, addPolicyDTO);

        // 쿠폰정책 검색 후 검증
        CouponPolicy couponPolicy = couponPolicyRepository.findByName("Test Policy");
        assertThat(couponPolicy).isNotNull();
        assertThat(couponPolicy.getName()).isEqualTo("Test Policy");
        assertThat(couponPolicy.getSaleType()).isEqualTo(SaleType.AMOUNT);

        DiscountRule discountRule = disCountRuleRepository.findBySaleAmount(10000);
        assertThat(discountRule).isNotNull();
        assertThat(discountRule.getSaleAmount()).isEqualTo(10000);

        // Check if CategoryPolicy and BookPolicy have been saved
        // Assuming CategoryPolicy and BookPolicy entities are properly linked to CouponPolicy
        assertThat(categoryPolicyRepository.findAll()).hasSize(2);
        assertThat(bookPolicyRepository.findAll()).hasSize(2);
    }

    @Test
    void testCouponUpdate() {
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

        // 쿠폰 정책 생성 및 책/카테고리 정책 생성
        adminCouponService.couponPolicyCreate(requestCouponPolicyDTO, discountRuleDTO, addPolicyDTO);

        // 수정할 요소
        Long userId = 1L;
        Long policyId = 1L;
        LocalDate currentDate = LocalDate.now();

        RequestUserCouponDTO requestUserCouponDTO = new RequestUserCouponDTO();
        requestUserCouponDTO.setUserId(List.of(userId));
        requestUserCouponDTO.setCouponPolicyId(policyId);
        requestUserCouponDTO.setStartDate(currentDate);
        requestUserCouponDTO.setEndDate(currentDate.plusDays(50));

        //유저 쿠폰 생성
        adminCouponService.userCouponCreate(requestUserCouponDTO);

        // 수정할 설정 생성
        UpdateUserCouponByUserIdDto dto = new UpdateUserCouponByUserIdDto();

        dto.setState(CouponState.TIMEOUT);
        dto.setPolicyId(1L);
        dto.setEndDate(LocalDate.now().plusDays(50));
        dto.setUserIds(Arrays.asList(1L, 2L));      //변경할 유저 ID

        // 사용자 쿠폰 수정
        adminCouponService.couponUpdate(dto);

        List<UserCoupon> userCoupon = couponRepository.findByUserId(userId);

        assertThat(userCoupon.get(0).getEndDate()).isEqualTo(currentDate.plusDays(50));
        assertThat(userCoupon.get(0).getPolicy().getId()).isEqualTo(1L);
        assertThat(userCoupon.get(0).getCouponState()).isEqualTo(CouponState.TIMEOUT);
    }

    //유저 쿠폰 생성 테스트
    @Test
    void testUserCouponCreate() {
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

        // 쿠폰 정책 생성 및 책/카테고리 정책 생성
        adminCouponService.couponPolicyCreate(requestCouponPolicyDTO, discountRuleDTO, addPolicyDTO);

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
    }

    @Test
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
