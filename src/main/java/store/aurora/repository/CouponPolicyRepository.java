package store.aurora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.aurora.dto.RequestCouponPolicyDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CouponPolicyRepository extends JpaRepository<RequestCouponPolicyDTO, Long> {

    Optional<RequestCouponPolicyDTO> findByCouponCode(String couponCode);

    List<RequestCouponPolicyDTO> findByUserId(long userId);

    List<RequestCouponPolicyDTO> findByStatusAndAssignedAtBefore(String status, LocalDateTime date);
}