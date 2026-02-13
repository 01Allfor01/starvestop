package com.allforone.starvestop.domain.payment.entity;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.event.DomainEvent;
import com.allforone.starvestop.domain.payment.event.PaymentStatusChangedEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(unique = true)
    private String paymentKey;

    @Column(nullable = false, unique = true)
    private String orderKey;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(nullable = false)
    private boolean stockReleased;

    @Column
    private LocalDateTime paymentAt;

    @Column
    private LocalDateTime canceledAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updated_at;

    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return events;
    }

    private Payment(Long userId, Order order, String orderKey, BigDecimal amount) {
        this.userId = userId;
        this.order = order;
        this.orderKey = orderKey;
        this.amount = amount;
        this.status = PaymentStatus.REQUESTED;
        this.stockReleased = false;
    }

    public void markRequestedEvent() {
        if (this.status != PaymentStatus.REQUESTED) return;

        domainEvents.add(PaymentStatusChangedEvent.of(
                this.id, this.orderKey, this.userId,
                PaymentStatus.REQUESTED,  null
        ));
    }

    public static Payment request(Long userId, Order order, String orderKey, BigDecimal amount) {
        return new Payment(userId, order, orderKey, amount);
    }

    private void requireStatus(PaymentStatus... allowed) {
        for (PaymentStatus s : allowed) {
            if (this.status == s) return;
        }
        throw new CustomException(ErrorCode.INVALID_PAYMENT_STATE);
    }

    public void pending() {
        requireStatus(PaymentStatus.REQUESTED, PaymentStatus.FAILED_RETRYABLE);
        this.status = PaymentStatus.PENDING;

        domainEvents.add(PaymentStatusChangedEvent.of(
                this.id, this.orderKey, this.userId,
                PaymentStatus.PENDING,  null
        ));
    }

    public void failRetryable(String payload) {
        requireStatus(PaymentStatus.PENDING);
        this.status = PaymentStatus.FAILED_RETRYABLE;

        domainEvents.add(PaymentStatusChangedEvent.of(
                this.id, this.orderKey, this.userId,
                PaymentStatus.FAILED_RETRYABLE,  payload
        ));
    }

    public void failNonRetryable(String payload) {
        requireStatus(PaymentStatus.PENDING);
        this.status = PaymentStatus.FAILED_NON_RETRYABLE;

        domainEvents.add(PaymentStatusChangedEvent.of(
                this.id, this.orderKey, this.userId,
                PaymentStatus.FAILED_NON_RETRYABLE,payload
        ));
    }

    public void markStockReleased() {
        if (this.stockReleased) return;
        this.stockReleased = true;
    }

    @Deprecated
    public void fail() {
        requireStatus(PaymentStatus.REQUESTED, PaymentStatus.PENDING);
        this.status = PaymentStatus.FAILED_NON_RETRYABLE;

        domainEvents.add(PaymentStatusChangedEvent.of(
                this.id, this.orderKey, this.userId,
                PaymentStatus.FAILED_NON_RETRYABLE, null
        ));
    }

    public void cancel() {
        requireStatus(PaymentStatus.REQUESTED, PaymentStatus.PENDING);
        this.canceledAt = LocalDateTime.now();
        this.status = PaymentStatus.CANCELED;

        domainEvents.add(PaymentStatusChangedEvent.of(
                this.id, this.orderKey, this.userId,
                PaymentStatus.CANCELED, null
        ));
    }

    public void success(String paymentKey) {
        requireStatus(PaymentStatus.REQUESTED, PaymentStatus.PENDING);
        this.paymentAt = LocalDateTime.now();
        this.paymentKey = paymentKey;
        this.status = PaymentStatus.SUCCEEDED;

        domainEvents.add(PaymentStatusChangedEvent.of(
                this.id, this.orderKey, this.userId,
                PaymentStatus.SUCCEEDED, null
        ));
    }
}
