package store.aurora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.aurora.dto.CouponDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<CouponDTO, Long> {

    Optional<CouponDTO> findByCouponCode(String couponCode);

    List<CouponDTO> findByUserId(long userId);

    List<CouponDTO> findByStatusAndAssignedAtBefore(String status, LocalDateTime date);
}