package store.aurora.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "book_policies")
@Data
public class BookPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String ruleCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "discount_rule_id", nullable = false)
    private DiscountRule discountRule;
    
}

