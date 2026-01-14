package com.allforone.starvestop.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을수 없습니다."),
    EMAIL_CONFLICT(HttpStatus.CONFLICT,"이미 존재하는 이메일 입니다.")

    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
