package com.allforone.starvestop.domain.payment.dto.response;

import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.enums.PurchaseType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetPaymentDetailsResponse {

    private final Long paymentId;
    private final String orderId;
    private final PaymentStatus status;
    private final Long purchaseId;
    private final String purchaseName;
    private final PurchaseType purchaseType;
    private final BigDecimal amount;
    private final LocalDateTime createdAt;

    public static GetPaymentDetailsResponse from(Payment payment) {
        return new GetPaymentDetailsResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getStatus(),
                payment.getPurchaseId(),
                payment.getPurchaseName(),
                payment.getPurchaseType(),
                payment.getAmount(),
                payment.getCreatedAt()
        );
    }
}
