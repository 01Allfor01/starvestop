package com.allforone.starvestop.domain.notification.entity;


import com.allforone.starvestop.domain.notification.enums.MealTimeBit;
import com.allforone.starvestop.domain.notification.enums.NotificationPlatformType;
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
    private int id;

    @Column(nullable = false)
    private Long userId;

    @JoinColumn()
    private Subscription subscription;

    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private MealTimeBit mealTime;

    @Column(updatable = false)
    private LocalDateTime created_at;

    @PrePersist
    public void prePersist() {
        this.created_at = LocalDateTime.now();
    }

    public NotificationJob(Long userId, MealTimeBit mealTime, UserRole role) {
        this.userId = userId;
        this.mealTime = mealTime;
        this.role = role;
    }

}

