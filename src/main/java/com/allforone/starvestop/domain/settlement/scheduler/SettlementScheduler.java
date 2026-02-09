package com.allforone.starvestop.domain.settlement.scheduler;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.settlement.service.SettlementService;
import com.allforone.starvestop.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SettlementScheduler {

    private final SettlementService settlementService;
    private final StoreService storeService;

    // 정책값: 수수료율 (예: 5% = 0.05)
    private static final BigDecimal FEE_RATE = new BigDecimal("0.05");


    //매월 1일 02:10 (KST) : 지난달 정산 생성
    //전체 매장 대상으로 생성 시도
    //이미 존재 / 결제없음은 스킵
    //그 외 예외는 로그 남기고 계속 진행
    @Scheduled(cron = "0 10 2 1 * *", zone = "Asia/Seoul")
    public void createLastMonthSettlements() {
        YearMonth lastMonth = YearMonth.from(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).minusMonths(1));

        List<Long> storeIds = storeService.findAllActiveStoreIds(); // 너희 기준에 맞게 구현
        if (storeIds.isEmpty()) {
            log.info("[SettlementScheduler] no stores. period={}", lastMonth);
            return;
        }

        int created = 0;
        int skipped = 0;
        int failed = 0;

        for (Long storeId : storeIds) {
            try {
                settlementService.createMonthly(storeId, lastMonth, FEE_RATE);
                created++;
            } catch (CustomException e) {
                // 스킵 대상은 조용히 넘김(운영 로그만)
                if (e.getErrorCode() == ErrorCode.SETTLEMENT_ALREADY_EXISTS ||
                        e.getErrorCode() == ErrorCode.SETTLEMENT_NO_TARGET_PAYMENTS) {
                    skipped++;
                    log.info("[SettlementScheduler] skipped. storeId={} period={} reason={}",
                            storeId, lastMonth, e.getErrorCode());
                    continue;
                }

                failed++;
                log.warn("[SettlementScheduler] failed(custom). storeId={} period={} code={}",
                        storeId, lastMonth, e.getErrorCode(), e);
            } catch (Exception e) {
                failed++;
                log.error("[SettlementScheduler] failed(unexpected). storeId={} period={}",
                        storeId, lastMonth, e);
            }
        }

        log.info("[SettlementScheduler] done. period={} created={} skipped={} failed={}",
                lastMonth, created, skipped, failed);
    }
}
