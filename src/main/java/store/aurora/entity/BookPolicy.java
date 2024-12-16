package store.aurora.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "book_policy")
@Data
public class BookPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_coupon")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private CouponPolicy policy;

    @Column(name = "book_id", nullable = false)
    private Long bookId;
}

