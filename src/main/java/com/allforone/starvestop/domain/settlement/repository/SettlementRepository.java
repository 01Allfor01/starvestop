package com.allforone.starvestop.domain.settlement.repository;

import com.allforone.starvestop.domain.settlement.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
    boolean existsByStoreIdAndPeriodYm(Long storeId, String periodYm);
}
