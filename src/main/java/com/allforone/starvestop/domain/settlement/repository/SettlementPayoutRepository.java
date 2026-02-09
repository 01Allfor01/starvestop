package com.allforone.starvestop.domain.settlement.repository;

import com.allforone.starvestop.domain.settlement.entity.SettlementPayout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementPayoutRepository extends JpaRepository<SettlementPayout, Long> {
}
