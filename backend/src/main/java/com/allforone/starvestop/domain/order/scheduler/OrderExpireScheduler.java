package com.allforone.starvestop.domain.order.scheduler;

import com.allforone.starvestop.domain.order.service.OrderUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderExpireScheduler {

    private final OrderUseCase orderUseCase;

    // 매 1분마다 실행 (서버 시간 기준)
    @Scheduled(fixedRate = 60 * 1000)
    public void cancelExpiredOrders() {
        int count = orderUseCase.cancelExpiredOrders(LocalDateTime.now());
        if (count > 0) {
            log.info("Canceled {} expired orders", count);
        }
    }
}
