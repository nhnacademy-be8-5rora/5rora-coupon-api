package store.aurora.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import store.aurora.entity.CouponState;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateUserCouponDto {
    @NotNull
    private List<Long> userCouponIds;    // 바꿀 유저 쿠폰 ID 리스트
    private long policyId; // 바꿀 정책의 ID
    private CouponState state;   // 변경할 쿠폰 상태
    private LocalDate startDate; // 시작일
    private LocalDate endDate;   // 종료일
}
