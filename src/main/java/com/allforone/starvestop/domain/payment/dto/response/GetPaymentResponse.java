package com.allforone.starvestop.domain.payment.dto.response;

import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.enums.PurchaseType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetPaymentResponse {

    private final String orderId;
    private final PaymentStatus paymentStatus;

    private final Long productId;
    private final String productName;
    private final PurchaseType purchaseType;
    private final BigDecimal productPrice;

    private final BigDecimal amount;

    public static GetPaymentResponse from(Payment payment) {

        return new GetPaymentResponse(
                payment.getOrderId(),
                payment.getStatus(),
                payment.getPurchaseId(),
                payment.getPurchaseName(),
                payment.getPurchaseType(),
                payment.getAmount(),
                payment.getAmount()
        );
    }
}
