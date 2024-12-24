package store.aurora.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import store.aurora.dto.*;
import store.aurora.domain.CouponPolicy;
import store.aurora.domain.CouponState;
import store.aurora.domain.DiscountRule;
import store.aurora.domain.SaleType;
import store.aurora.repository.*;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootTest
@Transactional
class AdminCouponServiceTest {

    @MockBean private CouponPolicyRepository couponPolicyRepository;
    @MockBean private CouponRepository couponRepository;
    @MockBean private DisCountRuleRepository disCountRuleRepository;
    @MockBean private CategoryPolicyRepository categoryPolicyRepository;
    @MockBean private BookPolicyRepository bookPolicyRepository;

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
        // Given
        RequestUserCouponDTO requestUserCouponDto = new RequestUserCouponDTO();
        requestUserCouponDto.setState(CouponState.LIVE);
        requestUserCouponDto.setCouponPolicyId(1L);
        requestUserCouponDto.setStartDate(LocalDate.now());
        requestUserCouponDto.setEndDate(LocalDate.now().plusDays(30));
        requestUserCouponDto.setUserId(Arrays.asList(1L, 2L, 3L));

        // When
        adminCouponService.userCouponCreate(requestUserCouponDto);

        // Then
        verify(couponRepository).saveAll(anyList()); // couponRepository의 saveAll 호출 여부 검증
    }
}
