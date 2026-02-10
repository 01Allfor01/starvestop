package com.allforone.starvestop.domain.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SearchPaymentLogResponse {
    private final Long id;
    private final Long paymentId;
    private final Long userId;
    private final String orderKey;
    private final String pgStatus;
    private final LocalDateTime timestamp;
}
