package store.aurora.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "discount_rule")
@Data
public class DiscountRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Long id;

    @Column(name = "need_cost")
    private long needCost;

    @Column(name = "max_sale")
    private long maxSale;

    @Column(name = "sale_percent")
    private Integer salePercent;

    @Column(name = "sale_amount")
    private Integer saleAmount;

}
