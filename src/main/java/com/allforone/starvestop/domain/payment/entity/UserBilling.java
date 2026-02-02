package com.allforone.starvestop.domain.payment.entity;

import com.allforone.starvestop.domain.payment.enums.BillingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_billings")
public class UserBilling {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private String customerKey;

    @Column(nullable = false)
    private String encryptedBillingKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingStatus status;

    private LocalDateTime registeredAt;
    private LocalDateTime lastUsedAt;

    private UserBilling(Long userId, String customerKey, String encryptedBillingKey, LocalDateTime now) {
        this.userId = userId;
        this.customerKey = customerKey;
        this.encryptedBillingKey = encryptedBillingKey;
        this.status = BillingStatus.ACTIVE;
        this.registeredAt = now;
    }

    public static UserBilling create(Long userId, String customerKey, String encryptedBillingKey, LocalDateTime now) {
        return new UserBilling(userId, customerKey, encryptedBillingKey, now);
    }

    public void markUsed(LocalDateTime now) {
        this.lastUsedAt = now;
    }

    public void deactivate(BillingStatus status) {
        this.status = status;
    }
}
