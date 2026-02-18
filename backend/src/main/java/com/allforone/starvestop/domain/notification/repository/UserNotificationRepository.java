package com.allforone.starvestop.domain.notification.repository;

import com.allforone.starvestop.domain.notification.dto.SendMealTimeNotificationDto;
import com.allforone.starvestop.domain.notification.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long>, UserNotificationRepositoryCustom {
    @Query("SELECT un FROM UserNotification un WHERE un.userId = :userId AND un.role = 'USER'")
    UserNotification findByUserId(Long userId);

    @Query("SELECT un.token FROM UserNotification un WHERE un.userId IN :usreIdList")
    List<String> findAllTokenByUserId(List<Long> userIdList);

    void deleteByTokenIn(List<String> tokenList);

    @Query(value = """
            SELECT un.user_id AS userId, un.token AS token, s.id AS subscriptionId, s.name AS subscriptionName
            FROM user_subscriptions us
            JOIN subscriptions s ON s.id = us.subscription_id
            JOIN user_notifications un ON un.user_id = us.user_id
              WHERE us.status = 'ACTIVE'
              AND (s.day & :day) <> 0
              AND un.token IS NOT NULL
            """, nativeQuery = true)
    List<SendMealTimeNotificationDto> findByTargetList(Integer day);

    void deleteByToken(String token);

    void deleteAllByTokenIn(List<String> tokenList);
}
