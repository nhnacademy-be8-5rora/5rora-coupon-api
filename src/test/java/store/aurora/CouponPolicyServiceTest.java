package store.aurora;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import store.aurora.dto.*;
import store.aurora.entity.CouponPolicy;
import store.aurora.entity.CouponState;
import store.aurora.entity.DiscountRule;
import store.aurora.entity.SaleType;
import store.aurora.repository.*;
import store.aurora.service.CouponPolicyService;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootTest
class CouponPolicyServiceTest {

    @MockBean private CouponPolicyRepository couponPolicyRepository;
    @MockBean private CouponRepository couponRepository;
    @MockBean private DisCountRuleRepository disCountRuleRepository;
    @MockBean private CategoryPolicyRepository categoryPolicyRepository;
    @MockBean private BookPolicyRepository bookPolicyRepository;

    @Autowired
    private CouponPolicyService couponPolicyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Mock 객체 초기화
    }

    @Test
    void testCouponPolicyCreate() {

        // Given
        RequestCouponPolicyDTO requestCouponPolicyDTO = new RequestCouponPolicyDTO();
        requestCouponPolicyDTO.setPolicyName("Test Policy");
        requestCouponPolicyDTO.setSaleType(SaleType.AMOUNT);

        DiscountRuleDTO discountRuleDTO = new DiscountRuleDTO();
        discountRuleDTO.setSaleAmount(10000);
        AddPolicyDTO addPolicyDTO = new AddPolicyDTO();
        addPolicyDTO.setCategoryId(Arrays.asList(1L, 2L));
        addPolicyDTO.setBookId(Arrays.asList(3L, 4L));

        // 사용자 쿠폰 생성
        couponPolicyService.couponPolicyCreate(requestCouponPolicyDTO, discountRuleDTO, addPolicyDTO);

        // Then
        verify(disCountRuleRepository).save(any(DiscountRule.class)); // DiscountRule 저장 검증
        verify(couponPolicyRepository).save(any(CouponPolicy.class)); // CouponPolicy 저장 검증
        verify(categoryPolicyRepository).saveAll(anyList()); // CategoryPolicy 저장 검증
        verify(bookPolicyRepository).saveAll(anyList()); // BookPolicy 저장 검증
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
        couponPolicyService.couponUpdate(dto);


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
        requestUserCouponDto.setPolicy(new CouponPolicy());
        requestUserCouponDto.setStartDate(LocalDate.now());
        requestUserCouponDto.setEndDate(LocalDate.now().plusDays(30));
        requestUserCouponDto.setUserId(Arrays.asList(1L, 2L, 3L));

        // When
        couponPolicyService.userCouponCreate(requestUserCouponDto);

        // Then
        verify(couponRepository).saveAll(anyList()); // couponRepository의 saveAll 호출 여부 검증
    }


}
