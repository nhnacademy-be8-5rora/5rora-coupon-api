package store.aurora.domain;

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
    private Long id;  // 정책 ID

    @Column(name = "policy_name", nullable = false)
    private String name;  // 정책 이름

    @Column(name = "sale_type", nullable = false)
    private SaleType saleType;  // 할인 종류

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", nullable = false)
    private DiscountRule discountRule;  // 할인 ID (옵션), discount 테이블과 외래 키 관계

    @OneToMany(mappedBy = "policy", fetch = FetchType.LAZY)
    private List<BookPolicy> bookPolicies;

    @OneToMany(mappedBy = "policy", fetch = FetchType.LAZY)
    private List<CategoryPolicy> categoryPolicies;

}
