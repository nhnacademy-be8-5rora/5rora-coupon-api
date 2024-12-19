package store.aurora.dto;

import lombok.Data;

@Data
public class DiscountRuleDto {
    private Integer needCost;
    private Integer maxSale;
    private Integer salePercent;
    private Integer saleAmount;
}
