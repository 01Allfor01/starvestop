package com.allforone.starvestop.domain.apilog.dto;

import com.allforone.starvestop.domain.apilog.entity.ApiLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetApiLogResponse {

    private final Long id;
    private final String userId;
    private final String userName;
    private final String userRole;
    private final String httpMethod;
    private final String requestUri;
    private final boolean isSuccess;
    private final Long execTime;
    private final LocalDateTime createdAt;

    public static GetApiLogResponse from(ApiLog apiLog) {
        return new GetApiLogResponse(
                apiLog.getId(),
                apiLog.getUserId(),
                apiLog.getUserName(),
                apiLog.getUserRole(),
                apiLog.getHttpMethod(),
                apiLog.getRequestUri(),
                apiLog.isSuccess(),
                apiLog.getExecTime(),
                apiLog.getCreatedAt()
        );
    }
}
