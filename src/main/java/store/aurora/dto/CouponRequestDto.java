package store.aurora.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CouponRequestDto {
    private List<Long> userId;   // 유저 식별자
    private Long policyId;
}

