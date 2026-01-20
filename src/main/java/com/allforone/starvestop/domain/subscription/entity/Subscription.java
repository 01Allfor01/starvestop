package com.allforone.starvestop.domain.subscription.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "subscriptions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscription extends BaseEntity {

    @Id
    @Column(name = "subscription_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "subscription_name", nullable = false, length = 100)
    private String subscriptionName;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false)
    private int day;

    @Column(nullable = false)
    private int mealTime;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Long stock;

    @Column(nullable = false)
    private boolean isJoinable;

    public Subscription(Store store, String subscriptionName, String description, int day, int mealTime, BigDecimal price, Long stock) {
        this.store = store;
        this.subscriptionName = subscriptionName;
        this.description = description;
        this.day = day;
        this.mealTime = mealTime;
        this.price = price;
        this.stock = stock;
        this.isJoinable = true;
    }

    public static Subscription create(Store store, String subscriptionName, String description, int day, int mealTime, BigDecimal price, Long stock) {
        return new Subscription(store, subscriptionName, description, day, mealTime, price, stock);
    }

    public void decrease(Long count) {
        if (this.stock == 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
        }
        this.stock -= count;
    }

    public void increase(Long count) {
        this.stock += count;
    }

    public void changeIsJoinable(boolean joinable) {
        this.isJoinable = joinable;
    }
}
