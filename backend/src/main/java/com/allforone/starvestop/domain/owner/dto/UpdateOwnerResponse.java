package com.allforone.starvestop.domain.owner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "판매자 정보 수정 응답")
@Getter
@AllArgsConstructor
public class UpdateOwnerResponse {
    @Schema(example = "owner@starvestop.com")
    private final String email;
    @Schema(example = "홍길동")
    private final String userName;
}
