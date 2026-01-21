package com.allforone.starvestop.domain.payment.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(unique = true)
    private String paymentKey;

    @Column(nullable = false, unique = true)
    private String orderKey;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column
    private LocalDateTime paymentAt;

    @Column
    private LocalDateTime canceledAt;

    public Payment(Order order,String paymentKey){
        this.order=order;
        this.paymentKey=paymentKey;
        this.orderKey=order.getKey();
        this.totalAmount=order.getTotalAmount();
        this.status = PaymentStatus.CREATED;
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
