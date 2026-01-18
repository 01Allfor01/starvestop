package com.allforone.starvestop.domain.payment.dto.response;

import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.enums.PurchaseType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetPaymentDetailsResponse {

    private final String orderId;
    private final PaymentStatus status;
    private final PurchaseType purchaseType;
    private final Long purchaseId;
    private final String purchaseName;
    private final LocalDateTime createdAt;

    public static GetPaymentDetailsResponse from(
            String orderId,
            PaymentStatus status,
            PurchaseType purchaseType,
            Long purchaseId,
            String purchaseName,
            LocalDateTime createdAt
    ) {
        return new GetPaymentDetailsResponse(
                orderId,
                status,
                purchaseType,
                purchaseId,
                purchaseName,
                createdAt
        );
    }
}
