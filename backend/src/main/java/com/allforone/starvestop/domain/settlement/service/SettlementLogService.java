package com.allforone.starvestop.domain.settlement.service;

import com.allforone.starvestop.domain.settlement.entity.SettlementLog;
import com.allforone.starvestop.domain.settlement.event.SettlementCreatedEvent;
import com.allforone.starvestop.domain.settlement.event.SettlementStatusChangedEvent;
import com.allforone.starvestop.domain.settlement.repository.SettlementLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementLogService {

    private final SettlementLogRepository settlementLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logStatusChange(SettlementStatusChangedEvent event) {
        if (settlementLogRepository.existsByEventId(event.eventId())) {
            return;
        }
        try {
            settlementLogRepository.save(SettlementLog.from(event));
        } catch (DataIntegrityViolationException e) {
            log.debug("duplicate settlement status log ignored. eventId={}", event.eventId());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logCreated(SettlementCreatedEvent event) {
        if (settlementLogRepository.existsByEventId(event.eventId())) {
            return;
        }
        try {
            settlementLogRepository.save(SettlementLog.from(event));
        } catch (DataIntegrityViolationException e) {
            log.debug("duplicate settlement created log ignored. eventId={}", event.eventId());
        }
    }
}
