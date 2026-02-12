package com.allforone.starvestop.common.enums;

import lombok.Getter;

@Getter
public enum SuccessMessage {
    //인증인가
    SIGN_UP_SUCCESS("회원가입 성공"),
    SIGN_UP_OWNER_SUCCESS("판매자 회원가입 성공"),
    SIGN_IN_SUCCESS("로그인 성공"),
    URL_RETURN_SUCCESS("로그인 URL 반환 성공"),

    //사용자
    USER_GET_SUCCESS("회원 조회 성공"),
    USER_UPDATE_SUCCESS("회원 정보 수정 성공"),
    USER_DELETE_SUCCESS("회원 탈퇴 성공"),

    //판매자
    OWNER_UPDATE_SUCCESS("판매자 정보 수정 성공"),
    OWNER_DELETE_SUCCESS("회원 탈퇴 성공"),

    //장바구니
    CART_CREATE_SUCCESS("장바구니 생성 성공"),
    CART_GET_SUCCESS("장바구니 조회 성공"),
    CART_UPDATE_SUCCESS("장바구니 수정 성공"),
    CART_DELETE_SUCCESS("장바구니 삭제 성공"),

    //사용자 구독
    USER_SUBSCRIPTION_CREATE_SUCCESS("구독 성공"),
    USER_SUBSCRIPTION_GET_SUCCESS("구독 조회 성공"),
    USER_SUBSCRIPTION_GET_DETAIL_SUCCESS("구독 상세 조회 성공"),
    USER_SUBSCRIPTION_CANCEL_SUCCESS("구독 취소 성공"),


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

    //주문
    ORDER_CREATE_SUCCESS("주문 생성 성공"),
    ORDER_GET_SUCCESS("주문 조회 성공"),
    ORDER_UPDATE_SUCCESS("주문 수정 성공"),
    ORDER_DELETE_SUCCESS("주문 삭제 성공"),

    //주문상품
    ORDER_PRODUCT_LIST_GET_SUCCESS("주문 상품 리스트 조회 성공"),

    //결제
    PAYMENT_CREATE_SUCCESS("결제 생성 성공"),
    PAYMENT_REQUIRE_SUCCESS("결제 요청 성공"),
    MY_PAYMENT_LIST_GET_SUCCESS("내 결제 내역 조회 성공"),
    PAYMENT_DETAIL_GET_SUCCESS("결제 상세 조회 성공"),
    BILLING_CONFIRM_SUCCESS("자동 결제 승인 성공"),

    //쿠폰
    COUPON_CREATE_SUCCESS("쿠폰 등록 성공"),
    COUPON_LIST_GET_SUCCESS("쿠폰 목록 조회 성공"),
    COUPON_DETAIL_GET_SUCCESS("쿠폰 상세 조회 성공"),
    COUPON_STATUS_UPDATE_SUCCESS("쿠폰 상태 수정 성공"),
    COUPON_DELETE_SUCCESS("쿠폰 삭제 성공"),

    //사용자 쿠폰
    USER_COUPON_CREATE_SUCCESS("사용자 쿠폰 등록 성공"),
    USER_COUPON_LIST_GET_SUCCESS("사용자 쿠폰 목록 조회 성공"),
    USER_COUPON_DETAIL_GET_SUCCESS("사용자 쿠폰 상세 조회 성공"),
    USER_COUPON_DELETE_SUCCESS("사용자 쿠폰 삭제 성공"),

    //결제 로그
    PAYMENT_LOG_GET_SUCCESS("결제 로그 조회 성공"),
    PAYMENT_LOG_SEARCH_SUCCESS("결제 로그 검색 성공"),

    // 영수증
    RECEIPT_GET_SUCCESS("영수증 조회 성공"),

    //s3
    PRESIGNED_URL_CREATE_SUCCESS("이미지 업로드 URL 생성 성공"),
    IMAGE_UPLOAD_SUCCESS("이미지 업로드 성공"),

    //api로그
    API_LOG_GET_SUCCESS("API 로그 조회 성공"),

    //채팅
    CHAT_ROOM_CREATE_SUCCESS("채팅방 생성 성공"),
    CHAT_ROOM_GET_SUCCESS("채팅방 목록 조회 성공"),
    CHAT_MESSAGE_GET_SUCCESS("채팅 메세지 조회 성공"),

    //FCM
    NOTIFICATION_TOKEN_STORE_SUCCESS("알림 토큰 저장 성공"),
    NOTIFICATION_SEND_SUCCESS("알림 전송 성공"),

    // 정산
    SETTLEMENT_CREATE_SUCCESS("정산 생성 성공");
    ;

    private final String message;

    SuccessMessage(String message) {
        this.message = message;
    }
}
