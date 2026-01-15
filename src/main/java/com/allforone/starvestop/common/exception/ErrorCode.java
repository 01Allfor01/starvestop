package com.allforone.starvestop.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //인증인가
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),
    //사용자
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을수 없습니다"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT,"이미 존재하는 이메일입니다"),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED,"입력하신 비밀번호가 일치하지 않습니다"),
    //사용자 구독

    //상품


    //매장
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 매장입니다"),

    //구독

    //결제
;
    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
