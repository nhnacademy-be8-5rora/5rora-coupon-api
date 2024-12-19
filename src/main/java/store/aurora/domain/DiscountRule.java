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

//    // sale_percent와 sale_amount 둘 중 하나는 null이 아니어야 한다는 조건
//    @AssertTrue(message = "Either salePercent or saleAmount must be provided.")
//    public boolean isSaleValid() {
//        return salePercent != null || saleAmount != null;
//    }
}
