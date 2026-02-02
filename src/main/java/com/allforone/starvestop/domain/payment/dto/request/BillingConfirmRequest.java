package com.allforone.starvestop.domain.payment.dto.request;

import lombok.Getter;

@Getter
public class BillingConfirmRequest {
    private String customerKey;
    private String authKey;
    private Long subscriptionId;
}
