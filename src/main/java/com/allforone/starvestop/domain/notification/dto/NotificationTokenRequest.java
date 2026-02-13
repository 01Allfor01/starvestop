package com.allforone.starvestop.domain.notification.dto;

import com.allforone.starvestop.domain.notification.enums.NotificationPlatformType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "FCM 토큰 저장 요청")
@Getter
@NoArgsConstructor
public class NotificationTokenRequest {
    @Schema(description = "FCM 토큰", example = "fcm_token_here")
    private String fcmToken;
    @Schema(description = "플랫폼", example = "WEB")
    private NotificationPlatformType platform;
}
