package store.aurora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import store.aurora.entity.BookPolicy;
import store.aurora.entity.CategoryPolicy;

@Repository
public interface BookPolicyRepository extends JpaRepository<BookPolicy, Long> {

}

