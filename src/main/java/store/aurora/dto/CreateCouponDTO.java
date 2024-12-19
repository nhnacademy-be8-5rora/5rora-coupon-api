package store.aurora.dto;

import lombok.Data;
import store.aurora.entity.CouponPolicy;

import java.util.List;

@Data
public class CreateCouponDTO {
    private List<Long> userId;   // 유저 식별자
    private CouponPolicy policy;
}
