package com.allforone.starvestop.domain.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationDto {
    String token;
    String title;
    String body;
}
