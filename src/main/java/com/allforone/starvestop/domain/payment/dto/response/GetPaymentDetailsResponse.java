package com.allforone.starvestop.domain.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetPaymentDetailsResponse {
    private final String orderId;
    private final String status;
    private final String productName;
    private final String productDescription;
    private final BigDecimal productPrice;
    private final BigDecimal productSalePrice;
    private final String subscriptionName;
    private final BigDecimal subscriptionAmount;
    private final String expiredAt;
    private final Boolean isExpired;
    private final String mealTime;
    private final LocalDateTime createdAt;
}
