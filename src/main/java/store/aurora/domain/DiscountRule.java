package store.aurora.domain;

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
    private Integer needCost;

    @Column(name = "max_sale")
    private Integer maxSale;

    @Column(name = "sale_percent")
    private Integer salePercent;

    @Column(name = "sale_amount")
    private Integer saleAmount;
}
