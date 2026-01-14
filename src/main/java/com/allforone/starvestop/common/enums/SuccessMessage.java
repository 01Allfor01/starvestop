package com.allforone.starvestop.common.enums;

import lombok.Getter;

@Getter
public enum SuccessMessage {

    SIGN_UP_SUCCESS("회원가입 성공"),     //이렇게?


    ;

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
