package com.allforone.starvestop.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //인증인가
    UN_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),
    USER_ROLE_CHANGE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "유저 권한 수정이 불가합니다"),

    //사용자
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을수 없습니다"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다"),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "입력하신 비밀번호가 일치하지 않습니다"),

    //사용자 구독
    USER_SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자 구독입니다"),

    //상품
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다"),

    //매장
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 매장입니다"),

    //구독
    SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 구독입니다"),

    //주문
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 주문입니다"),

    //결제
    PAYMENT_TARGET_REQUIRED(HttpStatus.BAD_REQUEST, "결제 대상이 필요합니다"),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제내역이 존재하지 않습니다"),
    PAYMENT_TARGET_AMBIGUOUS(HttpStatus.BAD_REQUEST, "상품과 구독 중 하나만 선택해주세요"),
    INVALID_PAYMENT_STATE(HttpStatus.CONFLICT, "잘못된 결제 상태입니다"),
    DUPLICATE_ORDER_ID(HttpStatus.CONFLICT, "이미 존재하는 주문 번호입니다"),


    //구매
    PURCHASE_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품 유형입니다"),
    INSUFFICIENT_STOCK(HttpStatus.CONFLICT, "상품의 재고가 부족합니다"),
    PAYMENT_EXPIRED(HttpStatus.GONE, "결제 유효 시간이 만료되었습니다"),
    PAYMENT_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "결제 정보 검증에 실패했습니다");


    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
