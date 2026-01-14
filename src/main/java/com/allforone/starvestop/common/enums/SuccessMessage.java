package com.allforone.starvestop.common.enums;

import lombok.Getter;

@Getter
public enum SuccessMessage {
    //인증인가
    SIGN_UP_SUCCESS("회원가입 성공"),
    SIGN_IN_SUCCESS("로그인 성공"),

    //사용자

    //사용자 구독

    //상품
    PRODUCT_CREATE_SUCCESS("상품 추가 성공"),

    //매장

    //구독
    SUBSCRIPTION_CREATE_SUCCESS("구독 생성 성공"),

    //결제

    ;

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
