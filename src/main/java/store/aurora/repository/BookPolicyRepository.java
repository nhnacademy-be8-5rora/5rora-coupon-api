package store.aurora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.aurora.entity.BookPolicy;
import store.aurora.entity.CategoryPolicy;

public interface BookPolicyRepository extends JpaRepository<BookPolicy, Long> {

}

