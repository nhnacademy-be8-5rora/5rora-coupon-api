package store.aurora.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import store.aurora.domain.CouponState;
import store.aurora.domain.UserCoupon;
import store.aurora.dto.ProductInfoDTO;
import store.aurora.dto.UserCouponDTO;
import store.aurora.mapper.UserCouponMapper;
import store.aurora.repository.CouponPolicyRepository;
import store.aurora.repository.UserCouponRepository;
import store.aurora.domain.CouponPolicy;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

import static org.mockito.Mockito.*;


@SpringBootTest
@Transactional
class CouponListServiceTest {

    @Autowired
    private CouponListService couponListService;

    @Mock
    private UserCouponRepository userCouponRepository;

    @InjectMocks
    private CouponListService couponListServiceUnderTest;

    private UserCoupon userCoupon;
    private UserCouponDTO userCouponDTO;

    @BeforeEach
    void setUp() {
        // Set up mock data
        userCoupon = new UserCoupon();
        userCoupon.setCouponId(1L);
        userCoupon.setUserId("user123");
        userCoupon.setPolicy(new CouponPolicy());

        // Assuming UserCouponMapper.toDTO method is available to convert to DTO
        userCouponDTO = UserCouponMapper.toDTO(userCoupon);
    }

    @Test
    void testGetCouponList() {
        // Arrange: Mock the repository call
        when(userCouponRepository.findByUserId("user123")).thenReturn(Collections.singletonList(userCoupon));

        // Act: Call the service method
        List<UserCouponDTO> result = couponListService.getCouponList("user123");

        // Assert: Verify that the result contains the correct data
        assertThat(result).isNotEmpty();  // Ensure the result is not empty
        assertThat(result.get(0).getCouponName()).isEqualTo("Coupon1");  // Check the coupon name in the DTO

        // Verify interaction with the repository
        verify(userCouponRepository, times(1)).findByUserId("user123");
    }

    @Test
    void testGetCouponList_noCoupons() {
        // Arrange: Mock the repository call to return an empty list
        when(userCouponRepository.findByUserId("user123")).thenReturn(List.of());

        // Act: Call the service method
        List<UserCouponDTO> result = couponListService.getCouponList("user123");

        // Assert: Verify that the result is empty
        assertThat(result).isEmpty();

        // Verify interaction with the repository
        verify(userCouponRepository, times(1)).findByUserId("user123");
    }
}