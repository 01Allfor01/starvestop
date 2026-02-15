package com.allforone.starvestop.domain.notification.entity;

import com.allforone.starvestop.common.enums.UserRole;
import com.allforone.starvestop.domain.notification.enums.NotificationPlatformType;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user_notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(unique = true)
    private String token;

    @Column(nullable = false)
    private NotificationPlatformType platform;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(updatable = false)
    private LocalDateTime created_at;

    @PrePersist
    public void prePersist() {
        this.created_at = LocalDateTime.now();
    }

    public UserNotification(Long userId, String token, NotificationPlatformType platform, UserRole role) {
        this.userId = userId;
        this.token = token;
        this.platform = platform;
        this.role = role;
    }

    public static UserNotification createToken(Long userId, String token, NotificationPlatformType platform, UserRole role) {
        return new UserNotification(
                userId, token, platform, role
        );
    }
}
