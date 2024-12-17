package store.aurora.dto;


import lombok.Data;
import store.aurora.entity.SaleType;

@Data
public class RequestCouponPolicyDTO {
    private String policyName;
    private SaleType saleType;
    private AddPolicyDTO addPolicyDTO;
    private DiscountRuleDTO discountRuleDTO;

}

