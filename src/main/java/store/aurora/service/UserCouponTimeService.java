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
        couponRepository.updateExpiredCoupons();
        log.info("Expired coupons updated at: {}", java.time.LocalDateTime.now());
    }
}