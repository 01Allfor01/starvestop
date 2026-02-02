package com.allforone.starvestop.domain.subscription.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.payment.entity.UserBilling;
import com.allforone.starvestop.domain.subscription.enums.UserSubscriptionStatus;
import com.allforone.starvestop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user_subscriptions",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_user_subscription",
                columnNames = {"user_id", "subscription_id"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSubscription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserSubscriptionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_billing_id")
    private UserBilling billing;

    private LocalDateTime expiresAt;

    public UserSubscription(User user, Subscription subscription) {
        this.user = user;
        this.subscription = subscription;
        this.status = UserSubscriptionStatus.PENDING;
    }

    public static UserSubscription create(User user, Subscription subscription) {
        return new UserSubscription(user, subscription);
    }

    public void activate(UserBilling billing) {
        if (this.status != UserSubscriptionStatus.PENDING) {
            throw new CustomException(ErrorCode.SUBSCRIPTION_INVALID_STATUS_TRANSITION);
        }
        if (billing == null) {
            throw new CustomException(ErrorCode.SUBSCRIPTION_BILLING_REQUIRED);
        }
        this.billing = billing;
        this.status = UserSubscriptionStatus.ACTIVE;
        this.expiresAt = LocalDateTime.now().plusMonths(1);
    }

    public boolean isDue() {
        return this.status == UserSubscriptionStatus.ACTIVE
                && this.expiresAt != null
                && !this.expiresAt.isAfter(LocalDateTime.now()); // expiresAt <= now
    }

    public void onChargeSuccess() {
        // 성공 시 1개월 연장
        this.status = UserSubscriptionStatus.ACTIVE;
        this.expiresAt = LocalDateTime.now().plusMonths(1);
    }

    public void onChargeFailed() {
        // 실패 처리
        this.status = UserSubscriptionStatus.PAYMENT_FAILED;
    }
}
