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
    private final String userName;
    private final String userRole;
    private final String clientIp;
    private final String httpMethod;
    private final String requestUri;
    private final String requestBody;
    private final String responseBody;
    private final boolean isSuccess;
    private final String errorMessage;
    private final Long execTime;
    private final LocalDateTime createdAt;

    public static GetApiLogDetailResponse from(ApiLog apiLog) {
        return new GetApiLogDetailResponse(
                apiLog.getId(),
                apiLog.getUserId(),
                apiLog.getUserName(),
                apiLog.getUserRole(),
                apiLog.getClientIp(),
                apiLog.getHttpMethod(),
                apiLog.getRequestUri(),
                apiLog.getRequestBody(),
                apiLog.getResponseBody(),
                apiLog.isSuccess(),
                apiLog.getErrorMessage(),
                apiLog.getExecTime(),
                apiLog.getCreatedAt()
        );
    }
}
