package store.aurora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.aurora.entity.DiscountRule;

public interface DisCountRuleRepository extends JpaRepository<DiscountRule, Long> {

}
