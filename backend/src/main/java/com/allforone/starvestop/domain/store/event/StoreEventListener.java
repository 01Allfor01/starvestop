package com.allforone.starvestop.domain.store.event;

import com.allforone.starvestop.domain.store.service.StoreRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreEventListener {

    private final StoreRedisService storeRedisService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUpsert(StoreGeoUpsertedEvent event) {
        try {
            storeRedisService.create(event.storeId(), event.lon(), event.lat());
        } catch (Exception e) {
            log.error("[Redis GEO] upsert failed. storeId={}", event.storeId(), e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDelete(StoreGeoDeletedEvent event) {
        try {
            storeRedisService.delete(event.storeId());
        } catch (Exception e) {
            log.error("[Redis GEO] delete failed. storeId={}", event.storeId(), e);
        }
    }
}
