package com.allforone.starvestop.domain.notification.repository;

import com.allforone.starvestop.domain.notification.entity.NotificationToken;

public interface NotificationTokenRepositoryCustom {
//    List<SendMealTimeNotificationDto> findByMealTime(Integer day, Integer mealTime);

    NotificationToken findOwnerTokenByOrderId(Long orderId);
}
