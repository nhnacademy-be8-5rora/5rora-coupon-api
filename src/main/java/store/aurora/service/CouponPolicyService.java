package store.aurora.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import store.aurora.dto.RequestCouponDto;
import store.aurora.dto.RequestCouponPolicyDTO;

import org.springframework.beans.factory.annotation.Autowired;
import store.aurora.exception.CouponInsertException;
import store.aurora.repository.CouponPolicyRepository;
import store.aurora.repository.CouponRepository;

import java.util.List;

@Service
public class CouponPolicyService {

    @Autowired
    private final CouponPolicyRepository couponPolicyRepository;

    @Autowired
    private final CouponRepository couponRepository;

    public CouponPolicyService(CouponPolicyRepository couponPolicyRepository, CouponRepository couponRepository) {
        this.couponPolicyRepository = couponPolicyRepository;
        this.couponRepository = couponRepository;
    }

    //쿠폰 정책 생성
    public void couponPolicyCreate(@Valid RequestCouponPolicyDTO requestCouponPolicyDTO) {

        couponPolicyRepository.flush();
    }

    public void couponPolicyAdd(@Valid RequestCouponPolicyDTO requestCouponPolicyDTO) {

        couponPolicyRepository.flush();
    }

    //사용자 쿠폰 수정
    public void couponUpdate(@Valid RequestCouponDto requestCouponDto) {

    }

    //사용자 쿠폰 생성
    public void userCouponCreate(RequestCouponDto requestCouponDto) {

    }

    //사용자 쿠폰 삭제 굳이 필요한가 싶어서 킵
    //    public void couponDelete(String couponId) {
    //
    //    }
}
