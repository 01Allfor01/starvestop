package com.allforone.starvestop.domain.notification.entity;


import com.allforone.starvestop.domain.notification.enums.DayBit;
import com.allforone.starvestop.domain.notification.enums.MealTimeBit;
import com.allforone.starvestop.domain.subscription.entity.Subscription;
import com.allforone.starvestop.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name="notification_jobs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationJob {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private String subscriptionName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MealTimeBit mealTime;

    @Column(updatable = false)
    private LocalDateTime created_at;

    @PrePersist
    public void prePersist() {
        this.created_at = LocalDateTime.now();
    }

    public NotificationJob(Long userId, String token, String subscriptionName, MealTimeBit mealTime) {
        this.userId = userId;
        this.token = token;
        this.subscriptionName = subscriptionName;
        this.mealTime = mealTime;
    }

}

