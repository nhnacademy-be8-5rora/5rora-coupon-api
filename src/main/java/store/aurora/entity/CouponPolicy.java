package store.aurora.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "coupon_policies")
@Data
public class CouponPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String couponCode;

    @Column(nullable = false)
    private String ruleCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "discount_rule_id", nullable = false)
    private DiscountRule discountRule;

    // Getters and Setters
}
