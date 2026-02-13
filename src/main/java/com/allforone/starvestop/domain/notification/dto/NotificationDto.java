package com.allforone.starvestop.domain.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "단일 알림 발송 요청")
@Getter
@NoArgsConstructor
public class NotificationDto {
    @Schema(description = "FCM 토큰", example = "fcm_token_here")
    String token;
    @Schema(description = "제목", example = "구독 알림")
    String title;
    @Schema(description = "본문", example = "오늘 점심 구독이 도착했어요!")
    String body;
}
