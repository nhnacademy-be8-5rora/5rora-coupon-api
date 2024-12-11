package store.aurora.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import store.aurora.dto.CouponRequestDto;
import store.aurora.entity.CouponPolicy;

@Service
public class CouponPolicyService {

    public void createCouponPolicy(CouponPolicy couponPolicy) {

    }

    public void couponCreate(@Valid CouponRequestDto couponRequestDto) {
    }

    public void couponUpdate(@Valid CouponRequestDto couponRequestDto, String couponId) {
    }

    public void couponDelete(String couponId) {
    }
}
