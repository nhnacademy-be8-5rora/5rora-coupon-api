package store.aurora.domain;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "category_policy")
@Data
public class CategoryPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_coupon")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private CouponPolicy policy;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

}
