package store.aurora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import store.aurora.entity.BookPolicy;
import store.aurora.entity.CouponPolicy;

@Repository
public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long> {

}

