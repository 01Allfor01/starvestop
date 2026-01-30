package com.allforone.starvestop.domain.apilog.dto;

import com.allforone.starvestop.domain.apilog.entity.ApiLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetApiLogDetailResponse {

    private final Long id;
    private final String userId;
    private final String userEmail;
    private final String userRole;
    private final String clientIp;
    private final String httpMethod;
    private final String requestUri;
    private final String params;
    private final String payload;
    private final boolean isSuccess;
    private final String errorMessage;
    private final Long execTime;
    private final LocalDateTime createdAt;

    public static GetApiLogDetailResponse from(ApiLog apiLog) {
        return new GetApiLogDetailResponse(
                apiLog.getId(),
                apiLog.getUserId(),
                apiLog.getUserEmail(),
                apiLog.getUserRole(),
                apiLog.getClientIp(),
                apiLog.getHttpMethod(),
                apiLog.getRequestUri(),
                apiLog.getParams(),
                apiLog.getPayload(),
                apiLog.isSuccess(),
                apiLog.getErrorMessage(),
                apiLog.getExecTime(),
                apiLog.getCreatedAt()
        );
    }
}
