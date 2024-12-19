package store.aurora.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import store.aurora.entity.SaleType;

@Data
public class RequestCouponPolicyDTO {
    
    @NotNull
    private String policyName;
    @NotNull
    private SaleType saleType;

    private AddPolicyDTO addPolicyDTO;

    @NotNull
    private DiscountRuleDTO discountRuleDTO;

}

