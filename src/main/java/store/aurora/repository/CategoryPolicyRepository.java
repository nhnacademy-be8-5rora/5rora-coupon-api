package store.aurora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.aurora.entity.CategoryPolicy;
import store.aurora.entity.DiscountRule;

public interface CategoryPolicyRepository extends JpaRepository<CategoryPolicy, Long> {

}

