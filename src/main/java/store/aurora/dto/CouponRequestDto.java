package store.aurora.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CouponRequestDto {
    private Long userId;   // 유저 식별자


}

