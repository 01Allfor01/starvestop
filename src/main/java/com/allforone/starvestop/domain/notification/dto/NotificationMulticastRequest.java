package com.allforone.starvestop.domain.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationMulticastRequest {
    String title;
    String body;
}
