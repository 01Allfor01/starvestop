package com.allforone.starvestop.domain.subscription.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.domain.store.entity.Store;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
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

    @Column(name = "subscription_name", nullable = false)
    @Size(max = 100)
    private String subscriptionName;

    @Column(nullable = false)
    @Size(max = 255)
    private String description;

    @Column(nullable = false)
    @Min(value = 1)
    @Max(value = 127)
    private int day;

    @Column(nullable = false)
    @Min(value = 1)
    @Max(value = 7)
    private int mealTime;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    @Max(value = 9999)
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

    public void changeIsJoinable(boolean joinable) {
        this.isJoinable = joinable;
    }
}
