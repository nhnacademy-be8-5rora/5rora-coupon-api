package store.aurora.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import store.aurora.dto.ProductInfoDTO;
import store.aurora.dto.UserCouponDTO;
import store.aurora.service.CouponListService;

import java.util.*;

@SpringBootTest
@Transactional
class UserCouponListControllerTest {

    @Autowired
    private UserCouponListController userCouponListController;

    @MockBean
    private CouponListService couponListService;

    private UserCouponDTO userCouponDTO;
    private ProductInfoDTO productInfoDTO;

    @BeforeEach
    public void setUp() {
        // Set up mock data for UserCouponDTO and ProductInfoDTO
        userCouponDTO = new UserCouponDTO();
        userCouponDTO.setCouponName("Coupon1");
        userCouponDTO.setSaleAmount(1000);

        productInfoDTO = new ProductInfoDTO();
        productInfoDTO.setProductId(1L);
        productInfoDTO.setPrice(100);
    }

    @Test
    void testCouponList() {
        // Arrange: Mock the service call
        when(couponListService.getCouponList("user123")).thenReturn(Collections.singletonList(userCouponDTO));

        // Act: Call the controller method
        ResponseEntity<List<UserCouponDTO>> response = userCouponListController.couponList("user123");

        // Assert: Verify that the response status is OK and the result contains the correct coupon name
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody().getFirst().getCouponName()).isEqualTo("Coupon1");

        // Verify the service method was called
        verify(couponListService).getCouponList("user123");
    }

    @Test
    void testProCouponList() {
        // Arrange: Mock the service call
        Map<Long, List<String>> couponListMap = new HashMap<>();
        couponListMap.put(1L, List.of("Coupon1"));
        when(couponListService.getCouponListByCategory(Collections.singletonList(productInfoDTO), "user123")).thenReturn(couponListMap);

        // Act: Call the controller method
        ResponseEntity<Map<Long, List<String>>> response = userCouponListController.proCouponList("user123", Collections.singletonList(productInfoDTO));

        // Assert: Verify that the response status is OK and the correct coupon list is returned
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody().get(1L)).contains("Coupon1");

        // Verify the service method was called
        verify(couponListService).getCouponListByCategory(Collections.singletonList(productInfoDTO), "user123");
    }
}