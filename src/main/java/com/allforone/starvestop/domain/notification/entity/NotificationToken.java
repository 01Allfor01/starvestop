package com.allforone.starvestop.domain.notification.entity;

import com.allforone.starvestop.domain.notification.enums.NotificationPlatformType;
import com.allforone.starvestop.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "notification_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(unique = true)
    private String token;

    @Column(nullable = false)
    private NotificationPlatformType platform;

    @Column(nullable = false)
    private UserRole role;

    @Column(updatable = false)
    private LocalDateTime created_at;

    @PrePersist
    public void prePersist() {
        this.created_at = LocalDateTime.now();
    }

    public NotificationToken(Long userId, String token, NotificationPlatformType platform, UserRole role) {
        this.userId = userId;
        this.token = token;
        this.platform = platform;
        this.role = role;
    }

    public static NotificationToken createToken(Long userId, String token, NotificationPlatformType platform, UserRole role) {
        return new NotificationToken(
                userId, token, platform, role
        );
    }
}
