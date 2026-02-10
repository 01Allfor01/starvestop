package com.allforone.starvestop.domain.notification.repository;

import com.allforone.starvestop.domain.notification.entity.NotificationJob;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationJobJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void bulkInsertIgnore(List<NotificationJob> jobs) {
        String sql = """
            INSERT IGNORE INTO notification_jobs
            (user_id, token, subscriptionName, meal_time, created_at)
            VALUES ( ?, ?, ?, ?, NOW())
        """;

        jdbcTemplate.batchUpdate(sql, jobs, 1000, (ps, j) -> {

            ps.setLong(1, j.getUserId());
            ps.setString(2, j.getToken());
            ps.setLong(3, j.getSubscriptionName());
            ps.setInt(4, j.getToken());
            ps.setString(9, j.title());
            ps.setString(10, j.body());
            ps.setTimestamp(11, java.sql.Timestamp.valueOf(j.nextAttemptAt()));
        });
    }
}
