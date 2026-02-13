package com.allforone.starvestop.domain.chat.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "메시지 발신자 타입")
public enum SenderType {
    @Schema(description = "일반 사용자")
    USER,
    @Schema(description = "매장 오너")
    OWNER
}
