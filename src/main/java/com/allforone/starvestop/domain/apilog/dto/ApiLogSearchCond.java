package com.allforone.starvestop.domain.apilog.dto;

import lombok.Getter;

@Getter
public class ApiLogSearchCond {

    private final String userId;
    private final String userEmail;
    private final String userRole;
    private final Long execTime;
    private final String httpMethod;
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
