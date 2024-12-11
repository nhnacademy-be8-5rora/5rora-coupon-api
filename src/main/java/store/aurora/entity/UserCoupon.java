package store.aurora.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_coupon")
@Data
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id", nullable = false)
    private Long couponId;

    @ManyToOne(fetch = FetchType.LAZY)  // Many UserCoupon -> One Policy
    @JoinColumn(name = "policy_id", nullable = false)  // 외래 키로 policy_id 지정
    private CouponPolicy policyId;

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_state", nullable = false)
    private CouponState couponState;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "used_period")
    private LocalDate usedPeriod;

    // Enum 클래스 정의
    public enum CouponState {
        LIVE, USED, TIMEOUT
    }
}
