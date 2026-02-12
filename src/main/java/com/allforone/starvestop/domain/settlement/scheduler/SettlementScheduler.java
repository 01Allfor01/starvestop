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
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SettlementScheduler {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final SettlementService settlementService;
    private final StoreService storeService;

    // 수수료율 5%
    private static final BigDecimal FEE_RATE = new BigDecimal("0.05");

    // 매월 1일 02:10 (KST) : 지난달 정산 생성
    @Scheduled(cron = "0 10 2 1 * *", zone = "Asia/Seoul")
    public void createLastMonthSettlements() {
        long start = System.currentTimeMillis();

        YearMonth lastMonth = YearMonth.now(KST).minusMonths(1);
        String periodYm = lastMonth.toString(); // "2026-01" 형태로 고정

        List<Long> storeIds = storeService.findAllActiveStoreIds();
        if (storeIds.isEmpty()) {
            log.info("[SettlementScheduler] no stores. periodYm={}", periodYm);
            return;
        }

        int created = 0;
        int skipped = 0;
        int failed = 0;

        for (Long storeId : storeIds) {
            try {
                settlementService.createMonthly(storeId, lastMonth, FEE_RATE, null);
                created++;
            } catch (CustomException e) {
                ErrorCode code = e.getErrorCode();

                // 스킵 대상은 stacktrace 없이 info 로깅
                if (code == ErrorCode.SETTLEMENT_ALREADY_EXISTS ||
                        code == ErrorCode.SETTLEMENT_NO_TARGET_PAYMENTS) {

                    skipped++;
                    log.info("[SettlementScheduler] skipped. storeId={} periodYm={} reason={}",
                            storeId, periodYm, code);
                    continue;
                }

                failed++;
                log.warn("[SettlementScheduler] failed(custom). storeId={} periodYm={} code={}",
                        storeId, periodYm, code, e);

            } catch (Exception e) {
                failed++;
                log.error("[SettlementScheduler] failed(unexpected). storeId={} periodYm={}",
                        storeId, periodYm, e);
            }
        }

        long elapsedMs = System.currentTimeMillis() - start;
        log.info("[SettlementScheduler] done. periodYm={} stores={} created={} skipped={} failed={} elapsedMs={}",
                periodYm, storeIds.size(), created, skipped, failed, elapsedMs);
    }
}
