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
    private DiscountRule discountRule;
    private CategoryPolicy categoryPolicy;
    private BookPolicy bookPolicy;

    public RequestCouponPolicyDTO(String policyName, SaleType saleType, DiscountRule discountRule) {
        this.policyName = policyName;
        this.saleType = saleType;
        this.discountRule = discountRule;
    }
}

