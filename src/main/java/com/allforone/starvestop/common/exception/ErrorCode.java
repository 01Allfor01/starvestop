package com.allforone.starvestop.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 인증인가
    UN_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),
    USER_ROLE_CHANGE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "유저 권한 수정이 불가합니다"),

    // 사용자
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을수 없습니다"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다"),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "입력하신 비밀번호가 일치하지 않습니다"),

    // 판매자
    OWNER_NOT_FOUND(HttpStatus.NOT_FOUND, "판매자를 찾을수 없습니다"),

    // 관리자
    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND,"관리자를 찾을 수 없습니다"),

    //장바구니
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니를 찾을 수 없습니다"),

    //사용자 구독
    USER_SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자 구독입니다"),
    USER_SUBSCRIPTION_EXIST(HttpStatus.CONFLICT, "이미 가입된 구독입니다"),

    // 상품
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다"),
    PRODUCT_NOT_ENOUGH_STOCK(HttpStatus.BAD_REQUEST, "남아있는 상품 수량이 부족합니다"),
    // 매장
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 매장입니다"),

    // 구독
    SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 구독입니다"),
    SUBSCRIPTION_OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "구독 재고가 부족합니다"),
    SUBSCRIPTION_INVALID_STATUS_TRANSITION
            (HttpStatus.CONFLICT, "현재 구독 상태에서는 이 작업을 수행할 수 없습니다"),
    SUBSCRIPTION_BILLING_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 결제 수단이 존재합니다"),
    SUBSCRIPTION_BILLING_REQUIRED(HttpStatus.CONFLICT, "자동 결제를 위해 결제 수단 등록이 필요합니다"),

    // 주문
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다"),

    // 결제
    PAYMENT_TARGET_REQUIRED(HttpStatus.BAD_REQUEST, "결제 대상이 필요합니다"),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제내역이 존재하지 않습니다"),
    PAYMENT_TARGET_AMBIGUOUS(HttpStatus.BAD_REQUEST, "상품과 구독 중 하나만 선택해주세요"),
    INVALID_PAYMENT_STATE(HttpStatus.CONFLICT, "잘못된 결제 상태입니다"),
    DUPLICATE_ORDER_ID(HttpStatus.CONFLICT, "이미 존재하는 주문 번호입니다"),
    BILLING_KEY_ISSUE_FAILED(HttpStatus.BAD_GATEWAY, "결제 수단 등록에 실패 하였습니다"),

    // 구매
    PURCHASE_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품 유형입니다"),
    INSUFFICIENT_STOCK(HttpStatus.CONFLICT, "상품의 재고가 부족합니다"),
    PAYMENT_EXPIRED(HttpStatus.GONE, "결제 유효 시간이 만료되었습니다"),
    PAYMENT_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "결제 정보 검증에 실패했습니다"),

    // 쿠폰
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다"),
    COUPON_MISSING_EXPIRATION(HttpStatus.BAD_REQUEST, "쿠폰 유효기간이 존재하지 않습니다"),
    COUPON_OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "쿠폰 재고가 부족합니다"),

    // 정산
    INVALID_SETTLEMENT_STATUS_TRANSITION(HttpStatus.CONFLICT, "정산 상태 전이가 올바르지 않습니다"),
    SETTLEMENT_ALREADY_EXISTS(HttpStatus.CONFLICT,"해당 기간의 정산이 이미 존재합니다"),
    SETTLEMENT_NO_TARGET_PAYMENTS(HttpStatus.CONFLICT,"해당 기간에 정산 가능한 결제가 존재하지 않습니다"),
    SETTLEMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"정산이 존재하지 않습니다"),

    //사용자 쿠폰
    USER_COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자 쿠폰입니다"),

    // 결제 로그
    PAYMENT_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "결제로그가 존재하지 않습니다"),

    // 영수증
    INVALID_RECEIPT_STATE(HttpStatus.CONFLICT, "현재 영수증 상태에서는 해당 요청을 처리할 수 없습니다"),
    RECEIPT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 영수증입니다"),

    // 락
    LOCK_GET_FAILED(HttpStatus.CONFLICT, "현재 처리 중입니다, 잠시후 다시 시도해주세요"),

    // s3
    IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다"),
    IMAGE_EXTENSION_BAD_REQUEST(HttpStatus.BAD_REQUEST, "지원하지 않는 확장자입니다"),

    //api로그
    API_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "API 로그가 존재하지 않습니다"),

    //채팅
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅 방이 존재하지 않습니다"),

    //FCM
    SECRET_FILE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "알림에 필요한 비밀 키를 찾을 수 없습니다"),
    INVALID_SECRET_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "알림에 필요한 비밀 키가 손상되었습니다"),
    NOTIFICATION_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "알림 전송에 실패했습니다"),
    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
