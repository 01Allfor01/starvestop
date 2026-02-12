package com.allforone.starvestop.domain.notification.repository;

import com.allforone.starvestop.domain.notification.entity.UserNotification;

public interface UserNotificationRepositoryCustom {
//    List<SendMealTimeNotificationDto> findByMealTime(Integer day, Integer mealTime);

    UserNotification findOwnerTokenByOrderId(Long orderId);
}
