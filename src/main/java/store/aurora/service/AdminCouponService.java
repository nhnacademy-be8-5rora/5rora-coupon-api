package store.aurora.service;

import store.aurora.dto.*;
import store.aurora.domain.CouponState;

public interface AdminCouponService {

    // 사용자 쿠폰 수정
    void couponUpdate(UpdateUserCouponByUserIdDto updateUserCouponByUserIdDto);

    // 사용자 쿠폰 생성
    void userCouponCreate(RequestUserCouponDTO requestUserCouponDTO);

    // 쿠폰 정책 생성
    void couponPolicyCreate(RequestCouponPolicyDTO requestCouponPolicyDTO,
                            DiscountRuleDTO discountRuleDTO,
                            AddPolicyDTO addPolicyDTO);
}
