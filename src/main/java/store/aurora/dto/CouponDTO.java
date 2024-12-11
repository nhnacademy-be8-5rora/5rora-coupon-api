package store.aurora.dto;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
public class CouponDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String couponCode;

    private LocalDateTime assignedAt;

    private LocalDateTime updatedAt;

    private String status;

    private Long userId;

    // getters and setters
}
