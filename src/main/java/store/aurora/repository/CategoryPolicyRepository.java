package store.aurora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import store.aurora.domain.CategoryPolicy;

@Repository
public interface CategoryPolicyRepository extends JpaRepository<CategoryPolicy, Long> {

}

