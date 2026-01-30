package com.allforone.starvestop.domain.apilog.repository;

import com.allforone.starvestop.domain.apilog.entity.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {
}
