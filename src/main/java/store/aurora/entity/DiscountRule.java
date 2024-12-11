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
    @Column(name = "discount_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private CouponPolicy policy;

    @Column(name = "need_cost")
    private long needCost;

    @Column(name = "max_sale")
    private long maxSale;

    @Column(name = "sale_percent")
    private Integer salePercent;

    @Column(name = "sale_amount")
    private Integer saleAmount;

}
