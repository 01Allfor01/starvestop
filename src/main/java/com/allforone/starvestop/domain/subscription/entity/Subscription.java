package com.allforone.starvestop.domain.subscription.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
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

    @Column(name = "subscription_name", nullable = false)
    private String subscriptionName;

    @Column(nullable = false)
    private BigDecimal price;

    public Subscription(Store store, String subscriptionName, BigDecimal price) {
        this.store = store;
        this.subscriptionName = subscriptionName;
        this.price = price;
    }

    public static Subscription create(Store store, String subscriptionName, BigDecimal price) {
        return new Subscription(store, subscriptionName, price);
    }
}
