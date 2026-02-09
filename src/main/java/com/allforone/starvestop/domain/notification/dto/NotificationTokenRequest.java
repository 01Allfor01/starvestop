package com.allforone.starvestop.domain.notification.dto;

import com.allforone.starvestop.domain.notification.enums.NotificationPlatformType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationTokenRequest {
    private String fcmToken;
    private NotificationPlatformType platform;
}
