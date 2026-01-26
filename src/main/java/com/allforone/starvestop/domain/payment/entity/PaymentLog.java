package com.allforone.starvestop.domain.payment.entity;

import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "payment_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long paymentId;

    private Long userId;

    private String orderKey;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String pgStatus;

    @Lob
    private String payload;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private PaymentLog(Long paymentId, Long userId, String orderKey, PaymentStatus paymentStatus, String pgStatus, String payload) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.orderKey = orderKey;
        this.paymentStatus = paymentStatus;
        this.pgStatus = pgStatus;
        this.payload = payload;
        this.timestamp = LocalDateTime.now();
    }

    public static PaymentLog create(Long paymentId, Long userId, String orderKey, PaymentStatus paymentStatus, String pgStatus, String payload) {
        return new PaymentLog(paymentId, userId, orderKey, paymentStatus, pgStatus, payload);
    }
}
