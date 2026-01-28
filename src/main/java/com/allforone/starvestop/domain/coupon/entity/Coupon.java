package com.allforone.starvestop.domain.coupon.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.domain.coupon.enums.CouponStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.allforone.starvestop.domain.coupon.enums.CouponStatus.USABLE;

@Getter
@Entity
@Table(name = "coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal discountAmount;

    @Column(nullable = false)
    private BigDecimal minAmount;

    private Integer validDays;

    private LocalDateTime expiresAt;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CouponStatus status;

    @Column(nullable = false)
    private Integer stock;

    public Coupon(String name, BigDecimal discountAmount, BigDecimal minAmount, Integer validDays, LocalDateTime expiresAt, Integer stock) {
        this.name = name;
        this.discountAmount = discountAmount;
        this.minAmount = minAmount;
        this.validDays = validDays;
        this.expiresAt = expiresAt;
        this.status = USABLE;
        this.stock = stock;
    }

    public static Coupon create(String name, BigDecimal discountAmount, BigDecimal minAmount, Integer validDays, LocalDateTime expiresAt, Integer stock) {
        return new Coupon(name, discountAmount, minAmount, validDays, expiresAt, stock);
    }

    public void update(CouponStatus status) {
        this.status = status;
    }
}
