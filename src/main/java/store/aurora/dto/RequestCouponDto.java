package store.aurora.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestCouponDto {
    private List<Long> userId;   // 유저 식별자
    private Long policyId;
}

