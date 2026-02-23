package com.allforone.starvestop.domain.notification.entity;


import com.allforone.starvestop.domain.notification.enums.MealTimeBit;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name="notification_jobs",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "nj_senddate_token_sub_meal",
                        columnNames = {"sendDate", "token", "subscriptionName", "mealTime"}
                )
        },
        indexes = {
                @Index(name = "idx_job_senddate_meal_id", columnList = "sendDate, mealTime, id"),
                @Index(name = "idx_job_token", columnList = "token")
        })
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

    @Column(nullable = false)
    private LocalDate sendDate;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public NotificationJob(Long userId, String token, String subscriptionName, MealTimeBit mealTime, LocalDate sendDate) {
        this.userId = userId;
        this.token = token;
        this.subscriptionName = subscriptionName;
        this.mealTime = mealTime;
        this.sendDate = sendDate;
    }
}

