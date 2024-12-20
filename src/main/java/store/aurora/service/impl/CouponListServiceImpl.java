package store.aurora.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.aurora.dto.ProductInfoDTO;
import store.aurora.domain.UserCoupon;
import store.aurora.repository.BookPolicyRepository;
import store.aurora.repository.CategoryPolicyRepository;
import store.aurora.repository.CouponRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponListServiceImpl {

    private final CouponRepository couponRepository;
    private final BookPolicyRepository bookPolicyRepository;
    private final CategoryPolicyRepository categoryPolicyRepository;


    //사용자 쿠폰 목록 검색
    public List<UserCoupon> getCouponList(Long userId) {

        return couponRepository.findByUserId(userId);
    }

    //결제창에서 각 상품별 쿠폰 목록
    public List<UserCoupon> getCouponListByCategory(List<ProductInfoDTO> productInfoDTO) {

        return null;
    }
}
