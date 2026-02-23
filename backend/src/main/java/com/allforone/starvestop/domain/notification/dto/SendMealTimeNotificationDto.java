package com.allforone.starvestop.domain.notification.dto;

public record SendMealTimeNotificationDto(Long cursorId, Long userId, String token, Long subscriptionId, String subscriptionName, Integer mealTimeBit) {}