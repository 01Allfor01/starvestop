package com.allforone.starvestop.domain.settlement.dto.response;

import com.allforone.starvestop.domain.settlement.entity.Settlement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateSettlementResponse {
    private final Long id;

    public static CreateSettlementResponse from(Settlement settlement) {
        return new CreateSettlementResponse(settlement.getId());
    }
}
