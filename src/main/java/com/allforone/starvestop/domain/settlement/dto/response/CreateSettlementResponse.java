package com.allforone.starvestop.domain.settlement.dto.response;

import com.allforone.starvestop.domain.settlement.entity.Settlement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "정산 생성 응답")
@Getter
@RequiredArgsConstructor
public class CreateSettlementResponse {
    @Schema(description = "정산 ID", example = "10")
    private final Long id;

    public static CreateSettlementResponse from(Settlement settlement){
        return new CreateSettlementResponse(settlement.getId());
    }
}
