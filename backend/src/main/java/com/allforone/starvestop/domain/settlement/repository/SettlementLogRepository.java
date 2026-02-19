package com.allforone.starvestop.domain.settlement.repository;

import com.allforone.starvestop.domain.settlement.entity.SettlementLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementLogRepository extends JpaRepository<SettlementLog, Long> {
    boolean existsByEventId(String eventId);
}
