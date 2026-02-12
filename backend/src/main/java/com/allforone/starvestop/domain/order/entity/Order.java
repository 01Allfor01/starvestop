package com.allforone.starvestop.domain.order.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.domain.coupon.entity.UserCoupon;
import com.allforone.starvestop.domain.order.enums.OrderStatus;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "user_coupon_id")
    private UserCoupon userCoupon;

    @Column(unique = true)
    private String orderKey;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal discountedPrice;

    @Column(nullable = false)
    private BigDecimal amount;

    private Order(Store store, String orderKey, User user, UserCoupon userCoupon, BigDecimal discountedPrice, BigDecimal amount) {
        this.store = store;
        this.status = OrderStatus.PENDING;
        this.orderKey = orderKey;
        this.user = user;
        this.userCoupon = userCoupon;
        this.discountedPrice = discountedPrice;
        this.amount = amount;
    }

    public static Order create(Store store, String orderKey, User user, UserCoupon userCoupon, BigDecimal discountedPrice, BigDecimal amount) {
        return new Order(store, orderKey, user, userCoupon, discountedPrice, amount);
    }

    public void paid() {
        this.status = OrderStatus.PAID;
    }
    public void cancel() {
        this.status = OrderStatus.CANCELED;
    }
}
