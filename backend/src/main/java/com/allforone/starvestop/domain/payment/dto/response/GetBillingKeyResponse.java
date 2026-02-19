package com.allforone.starvestop.domain.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetBillingKeyResponse {

    private final String billingKey;

    public static GetBillingKeyResponse of(String billingKey) {
        return new GetBillingKeyResponse(billingKey);
    }
}
