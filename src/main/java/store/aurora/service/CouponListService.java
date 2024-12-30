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

@Service
@RequiredArgsConstructor
public class CouponListService {

    private final UserCouponRepository userCouponRepository;
    private final CouponPolicyRepository couponPolicyRepository;

    //사용자ID로 해당 사용자가 가진 사용자 쿠폰 목록 검색
    @Transactional(readOnly = true)
    public List<UserCouponDTO> getCouponList(String userId) {
        List<UserCoupon> userCoupons =userCouponRepository.findByUserId(userId);

        // List<UserCoupon>을 List<UserCouponDTO>로 변환
        return userCoupons.stream()
                .map(UserCouponMapper::toDTO) // toDTO 메서드를 사용하여 변환
                .toList();
    }

    //결제창에서 각 상품별 사용 가능 쿠폰 목록
    @Transactional(readOnly = true)
    public Map<Long, List<String>> getCouponListByCategory(List<ProductInfoDTO> productInfoDTO, String userId) {
        Map<Long, List<String>> couponListMap = new HashMap<>();    //상품별 사용가능한 쿠폰먕

        //상품별 사용 가능한 쿠폰 리스트 생성 및 hashmap에 삽입
        for(ProductInfoDTO product : productInfoDTO){
            List<UserCoupon> couponListName = getAvailableCouponList(product, userId);
            List<String> couponList = new ArrayList<>();    //상품별 쿠폰명 리스트 생성

            //유저 리스트에서 상품명 리스트로 변환
            for(UserCoupon userCoupon : couponListName){
                couponList.add(userCoupon.getPolicy().getName());
            }

            couponListMap.put(product.getProductId(), couponList);
        }

        return couponListMap;
    }

    //ProductInfoDto(쿠폰 적용에 필요한 상품의 정보)
    public List<UserCoupon> getAvailableCouponList(ProductInfoDTO productInfoDTO, String userId) {
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
