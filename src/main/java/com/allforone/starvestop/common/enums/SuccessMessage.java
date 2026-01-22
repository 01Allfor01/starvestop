package com.allforone.starvestop.common.enums;

import lombok.Getter;

@Getter
public enum SuccessMessage {
    //인증인가
    SIGN_UP_SUCCESS("회원가입 성공"),
    SIGN_UP_OWNER_SUCCESS("판매자 회원가입 성공"),
    SIGN_IN_SUCCESS("로그인 성공"),
    USER_UPDATE_SUCCESS("회원 정보 수정 성공"),
    USER_DELETE_SUCCESS("회원 탈퇴 성공"),

    //사용자

    //사용자 구독
    USER_SUBSCRIPTION_CREATE_SUCCESS("사용자 구독 생성 성공"),
    USER_SUBSCRIPTION_GET_SUCCESS("사용자 구독 조회 성공"),
    USER_SUBSCRIPTION_DELETE_SUCCESS("사용자 구독 삭제 성공"),


    //상품
    PRODUCT_CREATE_SUCCESS("상품 추가 성공"),
    PRODUCT_LIST_BY_STORE_SUCCESS("매장 상품 목록 조회 성공"),
    PRODUCT_LIST_BY_SALE_SUCCESS("마감 세일 상품 목록 조회 성공"),
    PRODUCT_GET_SUCCESS("상품 조회 성공"),
    PRODUCT_UPDATE_SUCCESS("상품 수정 성공"),
    PRODUCT_DELETE_SUCCESS("상품 삭제 성공"),

    //매장
    STORE_CREATE_SUCCESS("매장 등록 성공"),
    STORE_UPDATE_SUCCESS("매장 정보 수정 성공"),
    STORE_DELETE_SUCCESS("매장 삭제 성공"),
    STORE_DETAIL_GET_SUCCESS("매장 상세 조회 성공"),
    STORE_LIST_GET_SUCCESS("매장 목록 조회 성공"),

    //구독
    SUBSCRIPTION_CREATE_SUCCESS("구독 생성 성공"),
    SUBSCRIPTION_GET_SUCCESS("구독 조회 성공"),
    SUBSCRIPTION_UPDATE_SUCCESS("구독 수정 성공"),
    SUBSCRIPTION_DELETE_SUCCESS("구독 삭제 성공"),


    //결제
    PAYMENT_CREATE_SUCCESS("결제 생성 성공"),
    PAYMENT_REQUIRE_SUCCESS("결제 요청 성공"),
    MY_PAYMENT_LIST_GET_SUCCESS("내 결제 내역 조회 성공"),
    PAYMENT_DETAIL_GET_SUCCESS("결제 상세 조회 성공"),

    //쿠폰
    COUPON_CREATE_SUCCESS("쿠폰 등록 성공"),


    //결제 로그
    PAYMENT_LOG_GET_SUCCESS("결제 로그 조회 성공");
    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
