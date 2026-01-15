package com.allforone.starvestop.domain.payment.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.usersubscription.entity.UserSubscription;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "payments")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_subscription_id")
    private UserSubscription userSubscription;

    @Column
    private String paymentKey;

    @Column(nullable = false, unique = true)
    private String orderId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column
    private LocalDateTime paymentAt;

    @Column
    private LocalDateTime canceledAt;

    private Payment(
            User user,
            Product product,
            UserSubscription userSubscription,
            String orderId,
            BigDecimal amount
    ) {
        this.user = user;
        this.product = product;
        this.userSubscription = userSubscription;
        this.orderId = orderId;
        this.amount = amount;
        this.status = PaymentStatus.CREATED;
    }

    public static Payment create(
            User user,
            Product product,
            UserSubscription userSubscription,
            String orderId,
            BigDecimal amount
    ) {
        return new Payment(
                user,
                product,
                userSubscription,
                orderId,
                amount
        );
    }

    private void requireStatus(PaymentStatus... allowed) {
        for (PaymentStatus s : allowed) {
            if (this.status == s) return;
        }
        throw new CustomException(ErrorCode.INVALID_PAYMENT_STATE);
    }

    public void requestPayment() {
        requireStatus(PaymentStatus.CREATED);
        this.status = PaymentStatus.REQUESTED;
    }

    public void pending() {
        requireStatus(PaymentStatus.REQUESTED);
        this.status = PaymentStatus.PENDING;
    }

    public void fail() {
        requireStatus(PaymentStatus.REQUESTED, PaymentStatus.PENDING);
        this.status = PaymentStatus.FAILED;
    }

    public void cancel() {
        requireStatus(PaymentStatus.CREATED, PaymentStatus.REQUESTED, PaymentStatus.PENDING);
        this.canceledAt = LocalDateTime.now();
        this.status = PaymentStatus.CANCELED;
    }

    public void success(String paymentKey) {
        requireStatus(PaymentStatus.REQUESTED, PaymentStatus.PENDING);
        this.paymentAt = LocalDateTime.now();
        this.paymentKey = paymentKey;
        this.status = PaymentStatus.SUCCEEDED;
    }
}
