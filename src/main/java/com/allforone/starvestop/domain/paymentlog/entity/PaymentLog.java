package com.allforone.starvestop.domain.paymentlog.entity;

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String pgStatus;

    @Lob
    private String payload;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
