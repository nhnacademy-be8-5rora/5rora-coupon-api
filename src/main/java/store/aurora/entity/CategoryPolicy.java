package store.aurora.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "category_policies")
@Data
public class CategoryPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String categoryCode;

    @Column(nullable = false)
    private String ruleCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "discount_rule_id", nullable = false)
    private DiscountRule discountRule;

    // Getters and Setters
}
