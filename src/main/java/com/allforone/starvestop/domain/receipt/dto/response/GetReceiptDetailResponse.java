package com.allforone.starvestop.domain.receipt.dto.response;

import com.allforone.starvestop.domain.orderproduct.dto.OrderProductDto;
import com.allforone.starvestop.domain.receipt.entity.Receipt;
import com.allforone.starvestop.domain.receipt.enums.ReceiptStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetReceiptDetailResponse {
    private final Long id;
    private final Long orderId;
    private final String orderKey;
    private final ReceiptStatus receiptStatus;
    private final BigDecimal amount;
    private final List<OrderProductDto> orderProducts;

    public static GetReceiptDetailResponse from(Receipt receipt, List<OrderProductDto> orderProducts) {
        return new GetReceiptDetailResponse(
                receipt.getId(),
                receipt.getOrder().getId(),
                receipt.getOrderKey(),
                receipt.getReceiptStatus(),
                receipt.getAmount(),
                orderProducts
        );
    }
}
