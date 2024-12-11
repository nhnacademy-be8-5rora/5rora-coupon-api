package store.aurora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.aurora.entity.CouponPolicy;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long> {

}