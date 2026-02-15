package com.allforone.starvestop.domain.settlement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;

@Schema(description = "정산 생성 요청")
@Getter
@NoArgsConstructor
public class CreateSettlementRequest {
    @Schema(description = "매장 ID", example = "1")
    private Long storeId;
    @Schema(description = "정산 대상 기간 (YYYY-MM 형식)", example = "2026-02")
    private YearMonth period;
    @Schema(description = "수수료율 (%)", example = "0.1")
    private BigDecimal feeRate;
}
