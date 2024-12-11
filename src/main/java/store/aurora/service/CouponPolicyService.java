package store.aurora.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import store.aurora.dto.CouponDTO;
import store.aurora.dto.CouponRequestDto;
import store.aurora.entity.CouponPolicy;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import store.aurora.exception.CouponInsertException;
import store.aurora.repository.CouponRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CouponPolicyService {

    @Autowired
    private CouponRepository couponRepository;


    public void batchInsert(List<CouponDTO> couponDTOList) throws CouponInsertException {
        try {
            couponRepository.saveAll(couponDTOList);
        } catch (Exception e) {
            throw new CouponInsertException("insertCoupon ERROR! 쿠폰 추가 메서드를 확인해주세요\n" + "Params : " + couponDTOList);
        }
    }

    public void createCouponPolicy(CouponPolicy couponPolicy) {

    }

    public void couponCreate(@Valid CouponRequestDto couponRequestDto) {
    }

    public void couponUpdate(@Valid CouponRequestDto couponRequestDto, String couponId) {
    }

    public void couponDelete(String couponId) {

    }

    public void userCouponCreate(CouponRequestDto couponRequestDto) {
    }
}
