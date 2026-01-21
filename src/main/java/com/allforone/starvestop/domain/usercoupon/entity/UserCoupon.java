package com.allforone.starvestop.domain.usercoupon.entity;

import com.allforone.starvestop.domain.coupon.entity.Coupon;
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
public class UserCoupon {
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
    private LocalDateTime started_at;

    @Column(nullable = false)
    private LocalDateTime expired_at;

    @Column(nullable = false)
    private LocalDateTime created_at;

    private LocalDateTime used_at;
}
