package store.aurora.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "coupon_policies")
@Data
public class CouponPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private Long id;  // Policy의 기본 키

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "policy")  // UserCoupon 엔티티의 policy 필드와 매핑
    private List<UserCoupon> userCoupons;  // 한 정책에 여러 개의 UserCoupon이 연결됨
}
