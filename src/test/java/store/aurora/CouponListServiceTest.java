package store.aurora;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import store.aurora.domain.CouponState;
import store.aurora.domain.UserCoupon;
import store.aurora.dto.ProductInfoDTO;
import store.aurora.repository.CouponRepository;
import store.aurora.service.CouponListService;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CouponListServiceTest {

    @Mock
    private CouponRepository couponRepository;

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

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(CouponState.LIVE, result.getFirst().getCouponState());
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

        // Assertions
        assertNotNull(result);
        assertTrue(result.containsKey(productInfoDTO.getProductId()));
        assertEquals(1, result.get(productInfoDTO.getProductId()).size());
        assertEquals(CouponState.LIVE, result.get(productInfoDTO.getProductId()).getFirst().getCouponState());

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

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(CouponState.LIVE, result.getFirst().getCouponState());

        verify(couponRepository, times(1)).findAvailableCoupons(userId, 123L, Arrays.asList(1L, 2L), 1000);
    }
}
