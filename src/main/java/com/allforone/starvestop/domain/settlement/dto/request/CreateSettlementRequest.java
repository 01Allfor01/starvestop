package com.allforone.starvestop.domain.settlement.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;

@Getter
@NoArgsConstructor
public class CreateSettlementRequest {
    private Long storeId;
    private YearMonth period;
    private BigDecimal feeRate;
}
