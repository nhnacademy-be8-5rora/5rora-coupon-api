package store.aurora.dto;

import lombok.Data;

@Data
public class DiscountRuleDTO {
    private Integer needCost;
    private Integer maxSale;
    private Integer salePercent;
    private Integer saleAmount;
}
