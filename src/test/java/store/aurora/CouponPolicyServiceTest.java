package store.aurora;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import store.aurora.dto.AddPolicyDTO;
import store.aurora.dto.DiscountRuleDTO;
import store.aurora.dto.RequestCouponPolicyDTO;
import store.aurora.dto.RequestUserCouponDto;
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

        // When
        couponPolicyService.couponPolicyCreate(requestCouponPolicyDTO, discountRuleDTO, addPolicyDTO);

        // Then
        verify(disCountRuleRepository).save(any(DiscountRule.class)); // DiscountRule 저장 검증
        verify(couponPolicyRepository).save(any(CouponPolicy.class)); // CouponPolicy 저장 검증
        verify(categoryPolicyRepository).saveAll(anyList()); // CategoryPolicy 저장 검증
        verify(bookPolicyRepository).saveAll(anyList()); // BookPolicy 저장 검증
    }

    @Test
    void testCouponUpdate() {
        // Given
        RequestUserCouponDto requestUserCouponDto = new RequestUserCouponDto();

        requestUserCouponDto.setState(CouponState.USED);
        requestUserCouponDto.setPolicy(new CouponPolicy());
        requestUserCouponDto.setStartDate(LocalDate.now().minusDays(5));
        requestUserCouponDto.setEndDate(LocalDate.now().plusDays(10));
        requestUserCouponDto.setUserId(Arrays.asList(1L, 2L));

        // When
        couponPolicyService.couponUpdate(requestUserCouponDto);

        // Then
        verify(couponRepository).updateCouponAttributesByUserIds(
                eq(CouponState.USED),
                anyLong(),
                eq(requestUserCouponDto.getStartDate()),
                eq(requestUserCouponDto.getEndDate()),
                eq(requestUserCouponDto.getUserId())
        ); // couponRepository의 updateCouponAttributesByUserIds 호출 여부 검증
    }

    @Test
    void testUserCouponCreate() {
        // Given
        RequestUserCouponDto requestUserCouponDto = new RequestUserCouponDto();
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
