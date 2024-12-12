package store.aurora.dto;

import jakarta.persistence.*;
import lombok.Data;
import store.aurora.entity.BookPolicy;
import store.aurora.entity.CategoryPolicy;
import store.aurora.entity.DiscountRule;
import store.aurora.entity.SaleType;

import java.time.LocalDateTime;

@Data
public class RequestCouponPolicyDTO {
    private String policyName;
    private SaleType saleType;
    private AddPolicyDTO addPolicyDTO;
    private DiscountRuleDTO discountRuleDTO;

//    private DiscountRule discountRule;
//    private CategoryPolicy categoryPolicy;
//    private BookPolicy bookPolicy;
}

