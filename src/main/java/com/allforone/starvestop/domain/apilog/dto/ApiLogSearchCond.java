package com.allforone.starvestop.domain.apilog.dto;

import lombok.Getter;

@Getter
public class ApiLogSearchCond {

    private final String userId;
    private final String userName;
    private final String userRole;
    private final String httpMethod;
    private final Boolean isSuccess;

    public ApiLogSearchCond(String userId, String userName, String userRole, String httpMethod, Boolean isSuccess) {
        this.userId = userId;
        this.userName = userName;
        this.userRole = userRole;
        this.httpMethod = httpMethod;
        this.isSuccess = isSuccess;
    }
}
