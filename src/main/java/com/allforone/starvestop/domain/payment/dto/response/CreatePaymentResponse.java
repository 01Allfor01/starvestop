//package com.allforone.starvestop.domain.payment.dto.response;
//
//import com.allforone.starvestop.domain.payment.entity.Payment;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//import java.math.BigDecimal;
//
//@Getter
//@AllArgsConstructor
//public class CreatePaymentResponse {
//    private final String orderId;
//    private final String purchaseName;
//    private final BigDecimal amount;
//
//    public static CreatePaymentResponse from(Payment payment) {
//        return new CreatePaymentResponse( payment.getOrderId(), payment.getPurchaseName(), payment.getAmount());
//    }
//}
