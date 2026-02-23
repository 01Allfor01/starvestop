package com.allforone.starvestop.domain.settlement.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "정산 처리 결과")
public enum SettlementAction {
    @Schema(description = "정산 생성 성공")
    CREATE_SUCCESS,

    @Schema(description = "정산 생성 실패")
    CREATE_FAILED,

    @Schema(description = "이미 생성되어 스킵됨")
    CREATE_SKIPPED
}
