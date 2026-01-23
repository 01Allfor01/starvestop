package com.allforone.starvestop.domain.receipt.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.receipt.enums.ReceiptStatus;
import com.allforone.starvestop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "receipts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Receipt extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private String orderKey;

    @Column(nullable = false)
    private String paymentKey;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReceiptStatus receiptStatus;

    @Column(nullable = false)
    private BigDecimal amount;

    private Receipt(User user, Order order, String orderKey, String paymentKey, BigDecimal amount) {
        this.user = user;
        this.order = order;
        this.orderKey = orderKey;
        this.paymentKey = paymentKey;
        this.receiptStatus = ReceiptStatus.VALID_PAYMENT;
        this.amount = amount;
    }

    public static Receipt create(User user, Order order, String orderKey, String paymentKey, BigDecimal amount) {
        return new Receipt(
                user,
                order,
                orderKey,
                paymentKey,
                amount);
    }

    public void requestRefund() {
        if (this.receiptStatus.equals(ReceiptStatus.VALID_PAYMENT)) {
            this.receiptStatus = ReceiptStatus.REFUND_IN_PROGRESS;
            return;
        }
        throw new CustomException(ErrorCode.INVALID_RECEIPT_STATE);
    }

    public void refunded() {
        if (this.receiptStatus.equals(ReceiptStatus.REFUND_IN_PROGRESS)) {
            this.receiptStatus = ReceiptStatus.REFUNDED;
            return;
        }
        throw new CustomException(ErrorCode.INVALID_RECEIPT_STATE);
    }
}
