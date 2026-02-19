package com.allforone.starvestop.domain.notification.repository;

import com.allforone.starvestop.domain.notification.entity.UserNotification;

import java.util.List;

public interface UserNotificationRepositoryCustom {

    UserNotification findOwnerTokenByOrderId(Long orderId);

    List<String> findUserTokenBySubscriptionId(Long subscriptionId);
}
