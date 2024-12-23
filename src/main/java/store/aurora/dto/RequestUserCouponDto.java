package store.aurora.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import store.aurora.entity.CouponPolicy;
import store.aurora.entity.CouponState;

import java.time.LocalDate;
import java.util.List;

@Data
public class RequestUserCouponDto {
    @NotNull
    private List<Long> userId;    // 유저 ID 리스트
    @NotNull
    private CouponPolicy policy; // 정책 정보

    private CouponState state;   // 쿠폰 상태
    private LocalDate startDate; // 시작일
    private LocalDate endDate;   // 종료일
}

