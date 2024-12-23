package store.aurora.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import store.aurora.dto.ProductInfoDTO;
import store.aurora.domain.UserCoupon;
import store.aurora.repository.BookPolicyRepository;
import store.aurora.repository.CategoryPolicyRepository;
import store.aurora.repository.CouponRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CouponListService {

    private final CouponRepository couponRepository;
    private final RestTemplate restTemplate;
    private final BookPolicyRepository bookPolicyRepository;
    private final CategoryPolicyRepository categoryPolicyRepository;


    //사용자ID로 해당 사용자가 가진 사용자 쿠폰 목록 검색
    public List<UserCoupon> getCouponList(Long userId) {

        return couponRepository.findByUserId(userId);
    }

    //결제창에서 각 상품별 사용 가능 쿠폰 목록
    public Map<Long, List<UserCoupon>>  getCouponListByCategory(List<ProductInfoDTO> productInfoDTO, long userId) {
        Map<Long, List<UserCoupon>> couponListMap = new HashMap<>();

        //각 상품마다 쿠폰 리스트 생성 및 hashmap에 삽입
        for(ProductInfoDTO product : productInfoDTO){
            List<UserCoupon> availableCouponList = getAvailableCouponList(product);
            couponListMap.put(product.getProductId(), availableCouponList);
        }

        return couponListMap;
    }

    public List<UserCoupon> getAvailableCouponList(ProductInfoDTO productInfoDTO) {

        return null;
    }
}
