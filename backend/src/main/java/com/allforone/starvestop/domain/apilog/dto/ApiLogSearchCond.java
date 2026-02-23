package com.allforone.starvestop.domain.apilog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "API 로그 검색 조건")
@Getter
public class ApiLogSearchCond {

    @Schema(description = "사용자 ID", example = "10", nullable = true)
    private final String userId;
    @Schema(description = "사용자 이메일", example = "user@starvestop.com", nullable = true)
    private final String userEmail;
    @Schema(description = "사용자 역할", example = "ADMIN", nullable = true)
    private final String userRole;
    @Schema(description = "실행 시간(ms) 이상", example = "200", nullable = true)
    private final Long execTime;
    @Schema(description = "HTTP Method", example = "GET", nullable = true)
    private final String httpMethod;
    @Schema(description = "성공 여부", example = "true", nullable = true)
    private final Boolean isSuccess;

    public ApiLogSearchCond(String userId, String userEmail, String userRole, Long execTime, String httpMethod, Boolean isSuccess) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userRole = userRole;
        this.execTime = execTime;
        this.httpMethod = httpMethod;
        this.isSuccess = isSuccess;
    }
}
