package com.allforone.starvestop.domain.settlement.event;

import com.allforone.starvestop.domain.settlement.service.SettlementLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SettlementEventHandler {

    private final SettlementLogService settlementLogService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(SettlementStatusChangedEvent event) {
        settlementLogService.logStatusChange(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(SettlementCreatedEvent event) {
        settlementLogService.logCreated(event);
    }
}
