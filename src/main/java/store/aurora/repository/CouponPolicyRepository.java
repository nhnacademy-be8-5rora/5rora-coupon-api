package store.aurora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.aurora.dto.RequestCouponPolicyDTO;

public interface CouponPolicyRepository extends JpaRepository<RequestCouponPolicyDTO, Long> {

}