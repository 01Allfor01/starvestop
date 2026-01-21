package com.allforone.starvestop.domain.subscription.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "subscriptions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscription extends BaseEntity {

    @Id
    @Column(name = "product_id")
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private int day;

    @Column(nullable = false)
    private int mealTime;

    @Column(nullable = false)
    private boolean isJoinable;

    public Subscription(int day, int mealTime) {
        this.day = day;
        this.mealTime = mealTime;
        this.isJoinable = true;
    }

    public static Subscription create(int day, int mealTime) {
        return new Subscription(day, mealTime);
    }

    public void changeIsJoinable(boolean joinable) {
        this.isJoinable = joinable;
    }
}
