package com.allforone.starvestop.domain.usersubscription.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.domain.subscription.entity.Subscription;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.usersubscription.enums.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user_subscriptions")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSubscription extends BaseEntity {

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
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    public UserSubscription(User user, Subscription subscription) {
        this.user = user;
        this.subscription = subscription;
        this.expiresAt = LocalDateTime.now().plusMonths(1);
        this.status = Status.PENDING;
    }

    public static UserSubscription create(User user, Subscription subscription) {
        return new UserSubscription(user, subscription);
    }
}
