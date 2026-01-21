//package com.allforone.starvestop.domain.payment.dto.response;
//
//import com.allforone.starvestop.domain.payment.entity.Payment;
//import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
//import com.allforone.starvestop.domain.payment.enums.PurchaseType;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//import java.math.BigDecimal;
//
//@Getter
//@AllArgsConstructor
//public class GetPaymentResponse {
//
//    private final Long paymentId;
//    private final String orderId;
//    private final PaymentStatus paymentStatus;
//    private final Long purchaseId;
//    private final String purchaseName;
//    private final PurchaseType purchaseType;
//    private final BigDecimal amount;
//
//    public static GetPaymentResponse from(Payment payment) {
//
//        return new GetPaymentResponse(
//                payment.getId(),
//                payment.getOrderId(),
//                payment.getStatus(),
//                payment.getPurchaseId(),
//                payment.getPurchaseName(),
//                payment.getPurchaseType(),
//                payment.getAmount()
//        );
//    }
//}
