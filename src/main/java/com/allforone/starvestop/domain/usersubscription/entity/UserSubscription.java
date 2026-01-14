package com.allforone.starvestop.domain.usersubscription.entity;

import com.allforone.starvestop.domain.subscription.entity.Subscription;
import com.allforone.starvestop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user_subscriptions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSubscription {

    @Id
    @Column(name = "user_subscription_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private int day;

    @Column(nullable = false)
    private int mealTime;
}
