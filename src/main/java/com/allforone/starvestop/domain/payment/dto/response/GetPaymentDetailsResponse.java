package com.allforone.starvestop.domain.payment.dto.response;

import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.product.dto.ProductInfo;
import com.allforone.starvestop.domain.subscription.dto.UserSubscriptionInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetPaymentDetailsResponse {

    private final String orderId;
    private final PaymentStatus status;
    private final ProductInfo productInfo;
    private final UserSubscriptionInfo userSubscriptionInfo;
    private final LocalDateTime createdAt;

    public static GetPaymentDetailsResponse from(
            String orderId,
            PaymentStatus status,
            ProductInfo productInfo,
            UserSubscriptionInfo userSubscriptionInfo,
            LocalDateTime createdAt
    ) {
        return new GetPaymentDetailsResponse(
                orderId,
                status,
                productInfo,
                userSubscriptionInfo,
                createdAt
        );
    }
}
