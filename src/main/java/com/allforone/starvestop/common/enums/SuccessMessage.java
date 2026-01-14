package com.allforone.starvestop.common.enums;

import lombok.Getter;

@Getter
public enum SuccessMessage {

    SIGN_UP_SUCCESS("회원가입 성공"),
    SIGN_IN_SUCCESS("로그인 성공"),


    ;

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
