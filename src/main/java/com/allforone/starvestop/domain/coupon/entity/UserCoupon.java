package com.allforone.starvestop.domain.coupon.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user_coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime usedAt;

    public UserCoupon(
            User user,
            Coupon coupon,
            LocalDateTime startedAt,
            LocalDateTime expiredAt
    ) {
        this.user = user;
        this.coupon = coupon;
        this.startedAt = startedAt;
        this.expiresAt = expiredAt;
    }

    public static UserCoupon create(
            User user,
            Coupon coupon,
            LocalDateTime startedAt,
            LocalDateTime expiresAt
    ) {
        return new UserCoupon(user, coupon, startedAt, expiresAt);
    }

    public void use() {
        this.usedAt = LocalDateTime.now();
    }
}
