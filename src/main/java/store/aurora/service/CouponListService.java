package store.aurora.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import store.aurora.entity.UserCoupon;
import store.aurora.repository.CouponRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponListService {

    private final CouponRepository couponRepository;


    public List<UserCoupon> getCouponList(Long userId) {

        return couponRepository.findByUserId(userId);
    }
}
