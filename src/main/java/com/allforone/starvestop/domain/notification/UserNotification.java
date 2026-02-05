package com.allforone.starvestop.domain.notification;

import com.allforone.starvestop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name="user_notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserNotification {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String token;

    @Column(nullable = false)
    private NotificationPlatformType platform;

    @Column(nullable = false)
    private LocalDateTime last_seen_at;

    @Column(updatable = false)
    private LocalDateTime created_at;

    @PrePersist
    public void prePersist() {
        this.created_at = LocalDateTime.now();
        this.last_seen_at = LocalDateTime.now();
    }

    public UserNotification(User user, String token, NotificationPlatformType platform) {
        this.user = user;
        this.token = token;
        this.platform = platform;
    }

    public static UserNotification create(User user, String token, NotificationPlatformType platform) {
        return new UserNotification(
            user, token, platform
        );
    }
}
