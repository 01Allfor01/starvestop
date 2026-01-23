package com.allforone.starvestop.domain.receipt.dto.response;

import com.allforone.starvestop.domain.receipt.entity.Receipt;
import com.allforone.starvestop.domain.receipt.enums.ReceiptStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetReceiptResponse {
    private final Long id;
    private final Long orderId;
    private final String orderKey;
    private final ReceiptStatus receiptStatus;
    private final BigDecimal amount;

    public static GetReceiptResponse from(Long userId, Receipt receipt) {
        return new GetReceiptResponse(
                userId,
                receipt.getOrder().getId(),
                receipt.getOrderKey(),
                receipt.getReceiptStatus(),
                receipt.getAmount());
    }
}
