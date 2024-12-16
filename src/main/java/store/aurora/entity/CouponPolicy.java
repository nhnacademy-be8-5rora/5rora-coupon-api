package store.aurora.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "coupon_policy")
@Data
public class CouponPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private Long Id;  // 정책 ID

    @Column(name = "policy_name", nullable = false)
    private String Name;  // 정책 이름

    @Column(name = "sale_type", nullable = false)
    private SaleType saleType;  // 할인 종류

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", referencedColumnName = "discount_id")
    private DiscountRule discountRule;  // 할인 ID (옵션), discount 테이블과 외래 키 관계

}
