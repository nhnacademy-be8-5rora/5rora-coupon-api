package store.aurora;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import store.aurora.domain.CouponState;
import store.aurora.domain.UserCoupon;
import store.aurora.dto.ProductInfoDTO;
import store.aurora.repository.CouponPolicyRepository;
import store.aurora.repository.CouponRepository;
import store.aurora.service.CouponListService;
import store.aurora.domain.CouponPolicy;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

import static org.mockito.Mockito.*;


class CouponListServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponPolicyRepository couponPolicyRepository;

    @InjectMocks
    private CouponListService couponListService;

    private ProductInfoDTO productInfoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 예시 ProductInfoDTO 객체 설정
        productInfoDTO = new ProductInfoDTO();
        productInfoDTO.setProductId(1L);
        productInfoDTO.setCategoryIds(Arrays.asList(1L, 2L));
        productInfoDTO.setBookId(123L);
        productInfoDTO.setPrice(1000);
    }

    @Test
    void testGetCouponList() {
        Long userId = 1L;

        // Mocking repository method
        List<UserCoupon> mockUserCoupons = new ArrayList<>();
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setCouponState(CouponState.LIVE);
        mockUserCoupons.add(userCoupon);

        when(couponRepository.findByUserId(userId)).thenReturn(mockUserCoupons);

        // Service method call
        List<UserCoupon> result = couponListService.getCouponList(userId);

        // Assertions using assertThat
        assertThat(result)
                .isNotNull()
                .hasSize(1);
        assertThat(result.getFirst().getCouponState()).isEqualTo(CouponState.LIVE);
        verify(couponRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetCouponListByCategory() {
        Long userId = 1L;

        // Mocking repository method
        List<UserCoupon> mockUserCoupons = new ArrayList<>();
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setCouponState(CouponState.LIVE);
        mockUserCoupons.add(userCoupon);

        // Mock for getAvailableCouponList method
        when(couponRepository.findAvailableCoupons(userId, 123L, Arrays.asList(1L, 2L), 1000))
                .thenReturn(mockUserCoupons);

        List<ProductInfoDTO> productInfoDTOList = Collections.singletonList(productInfoDTO);

        // Service method call
        Map<Long, List<UserCoupon>> result = couponListService.getCouponListByCategory(productInfoDTOList, userId);

        // Assertions using assertThat
        assertThat(result)
                .isNotNull()
                .containsKey(productInfoDTO.getProductId());
        assertThat(result.get(productInfoDTO.getProductId())).hasSize(1);
        assertThat(result.get(productInfoDTO.getProductId()).getFirst().getCouponState()).isEqualTo(CouponState.LIVE);

        verify(couponRepository, times(1)).findAvailableCoupons(userId, 123L, Arrays.asList(1L, 2L), 1000);
    }

    @Test
    void testGetAvailableCouponList() {
        Long userId = 1L;

        // Mocking repository method
        List<UserCoupon> mockUserCoupons = new ArrayList<>();
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setCouponState(CouponState.LIVE);
        mockUserCoupons.add(userCoupon);

        // Mock for getAvailableCouponList
        when(couponRepository.findAvailableCoupons(userId, 123L, Arrays.asList(1L, 2L), 1000))
                .thenReturn(mockUserCoupons);

        // Service method call
        List<UserCoupon> result = couponListService.getAvailableCouponList(productInfoDTO, userId);

        // Assertions using assertThat
        assertThat(result)
                .isNotNull()
                .hasSize(1);
        assertThat(result.getFirst().getCouponState()).isEqualTo(CouponState.LIVE);

        verify(couponRepository, times(1)).findAvailableCoupons(userId, 123L, Arrays.asList(1L, 2L), 1000);
    }

    @Test
    void testGetAvailableCouponListByCategory() {
        CouponPolicy policy1 = new CouponPolicy();
        policy1.setId(1L);
        policy1.setName("Policy 1");

        CouponPolicy policy2 = new CouponPolicy();
        policy2.setId(2L);
        policy2.setName("Policy 2");

        List<CouponPolicy> mockPolicies = List.of(policy1, policy2);

        when(couponPolicyRepository.findAll()).thenReturn(mockPolicies);

        List<CouponPolicy> result = couponListService.couponPolicyList();

        assertThat(result)
                .hasSize(2) // 반환된 리스트 크기 확인
                .contains(policy1, policy2) // 리스트에 포함된 객체 확인
                .first() // 첫 번째 요소에 대한 assertion 체인 시작
                .extracting(CouponPolicy::getName) // `getName()` 값을 추출
                .isEqualTo("Policy 1"); // 첫 번째 정책 이름 확인

    }
}