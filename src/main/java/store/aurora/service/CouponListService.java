package store.aurora.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.aurora.domain.CouponPolicy;
import store.aurora.dto.ProductInfoDTO;
import store.aurora.domain.UserCoupon;
import store.aurora.dto.UserCouponDTO;
import store.aurora.mapper.UserCouponMapper;
import store.aurora.repository.CouponPolicyRepository;
import store.aurora.repository.UserCouponRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponListService {

    private final UserCouponRepository userCouponRepository;
    private final CouponPolicyRepository couponPolicyRepository;

    //사용자ID로 해당 사용자가 가진 사용자 쿠폰 목록 검색
    @Transactional(readOnly = true)
    public List<UserCouponDTO> getCouponList(Long userId) {
        List<UserCoupon> userCoupons =userCouponRepository.findByUserId(userId);

        // List<UserCoupon>을 List<UserCouponDTO>로 변환
        return userCoupons.stream()
                .map(UserCouponMapper::toDTO) // toDTO 메서드를 사용하여 변환
                .collect(Collectors.toList());
    }

    //결제창에서 각 상품별 사용 가능 쿠폰 목록
    @Transactional(readOnly = true)
    public Map<Long, List<String>> getCouponListByCategory(List<ProductInfoDTO> productInfoDTO, long userId) {
        Map<Long, List<String>> couponListMap = new HashMap<>();

        //상품별 사용 가능한 쿠폰 리스트 생성 및 hashmap에 삽입
        for(ProductInfoDTO product : productInfoDTO){
            List<UserCoupon> couponListName = getAvailableCouponList(product, userId);
            List<String> couponList = new ArrayList<>();

            for(UserCoupon userCoupon : couponListName){
                couponList.add(userCoupon.getPolicy().getName());
            }

            couponListMap.put(product.getProductId(), couponList);
        }

        return couponListMap;
    }

    //ProductInfoDto(쿠폰 적용에 필요한 상품의 정보)
    public List<UserCoupon> getAvailableCouponList(ProductInfoDTO productInfoDTO, long userId) {
        Long bookId = productInfoDTO.getBookId();
        List<Long> categoryIds = productInfoDTO.getCategoryIds();
        Integer totalPrice = productInfoDTO.getPrice();

        return userCouponRepository.findAvailableCoupons(userId, bookId, categoryIds, totalPrice);
    }

    //쿠폰 정책 리스트 출력
    @Transactional(readOnly = true)
    public List<CouponPolicy> couponPolicyList() {
        return couponPolicyRepository.findAll();
    }
}
