package store.aurora.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "discount_rules")
@Data
public class DiscountRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ruleCode;

    @Column(nullable = false)
    private Double discountRate;

    @Column(nullable = false)
    private LocalDateTime validFrom;

    @Column(nullable = false)
    private LocalDateTime validUntil;

    @OneToMany(mappedBy = "discountRule", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookPolicy> bookPolicies;

    @OneToMany(mappedBy = "discountRule", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CategoryPolicy> categoryPolicies;

    // Getters and Setters
}
