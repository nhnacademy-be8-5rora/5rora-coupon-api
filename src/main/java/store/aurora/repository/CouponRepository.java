package store.aurora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.aurora.entity.UserCoupon;

public interface CouponRepository extends JpaRepository<UserCoupon, Long> {

}
