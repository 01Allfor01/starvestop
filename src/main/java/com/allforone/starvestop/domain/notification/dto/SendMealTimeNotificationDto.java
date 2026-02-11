package com.allforone.starvestop.domain.notification.dto;

import com.allforone.starvestop.domain.notification.enums.MealTimeBit;

public record SendMealTimeNotificationDto(Long id, Long userId, String token, String subscriptionName, MealTimeBit mealTime) {}