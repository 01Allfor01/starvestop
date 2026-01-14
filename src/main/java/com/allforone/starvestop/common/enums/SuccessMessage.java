package com.allforone.starvestop.common.enums;

import lombok.Getter;

@Getter
public enum SuccessMessage {

    SIGN_UP_SUCCESS("회원가입 성공"),     //이렇게?
    STORE_CREATE_SUCCESS("매장 등록 성공")

    ;

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
