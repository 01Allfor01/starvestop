package com.allforone.starvestop.domain.notification.repository;

import com.allforone.starvestop.domain.notification.dto.SendMealTimeNotificationDto;
import com.allforone.starvestop.domain.notification.entity.NotificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationTokenRepository extends JpaRepository<NotificationToken, Long>, NotificationTokenRepositoryCustom {
    @Query("SELECT un FROM NotificationToken un WHERE un.userId = :userId AND un.role = 'USER'")
    NotificationToken findByUserId(Long userId);

    @Query("SELECT un.token FROM NotificationToken un WHERE un.userId IN :usreIdList")
    List<String> findAllTokenByUserId(List<Long> userIdList);

    void deleteByTokenIn(List<String> tokenList);

    @Query(value = """
        SELECT nt.user_id AS userId, nt.token AS token, s.id AS subscriptionId, s.name AS subscriptionName
        FROM user_subscriptions us
        JOIN subscriptions s ON s.id = us.subscription_id
        JOIN notification_tokens nt ON nt.user_id = us.user_id
          WHERE us.status = 'ACTIVE'
          AND (s.day & :day) <> 0
          AND nt.token IS NOT NULL
        """, nativeQuery = true)
    List<SendMealTimeNotificationDto> findByTargetList(Integer day);

    void deleteByToken(String token);

}
