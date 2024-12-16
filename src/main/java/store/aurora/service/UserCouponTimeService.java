package store.aurora.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.aurora.repository.CouponRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCouponTimeService {

    private final CouponRepository couponRepository;

    // 매일 정오에 실행되는 스케줄러
    @Scheduled(cron = "0 0 12 * * ?") // 매일 12:00 PM 실행
    @Transactional
    public void updateExpiredCoupons() {
        //live 인 쿠폰 중에서 만료기간보다 현재기간이 큰 경우에만 couponState 을 timeout 으로 변경하는 쿼리
        couponRepository.updateExpiredCoupons();
        log.info("Expired coupons updated at: {}", java.time.LocalDateTime.now());
    }

    @Scheduled(cron = "0 0 12 * * ?") // 매일 12:00 PM 실행
    @Transactional
    public void deleteExpiredCoupons() {
        couponRepository.deleteExpiredCoupons();
        log.info("Expired coupons delete at: {}", java.time.LocalDateTime.now());
    }
}