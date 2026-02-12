package com.allforone.starvestop.domain.notification.repository;

import com.allforone.starvestop.domain.notification.dto.SendMealTimeNotificationDto;
import com.allforone.starvestop.domain.notification.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long>, UserNotificationRepositoryCustom {
    @Query("SELECT un FROM UserNotification un WHERE un.user.id = :userId")
    UserNotification findByUserId(Long userId);

    @Query("SELECT un.token FROM UserNotification un WHERE un.user.id IN :usreIdList")
    List<String> findAllTokenByUserId(List<Long> userIdList);


    @Query(value = """
            SELECT un.token AS token, s.name AS subscriptionName
            FROM user_subscriptions us
            JOIN subscriptions s ON s.id = us.subscription_id
            JOIN user_notifications un ON un.user_id = us.user_id
            WHERE us.status = 'ACTIVE'
              AND (s.day & :day) <> 0
              AND (s.meal_time & :mealTime) <> 0
              AND un.token IS NOT NULL
            """, nativeQuery = true)
    List<SendMealTimeNotificationDto> findByMealTime(Integer day, Integer mealTime);

    void deleteByToken(String token);
}
