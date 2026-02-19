package com.allforone.starvestop.domain.payment.dto.response;

import com.allforone.starvestop.domain.order.dto.OrderProductDto;
import com.allforone.starvestop.domain.payment.entity.Receipt;
import com.allforone.starvestop.domain.payment.enums.ReceiptStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetReceiptDetailResponse {
    private final Long id;
    private final Long orderId;
    private final String orderKey;
    private final ReceiptStatus receiptStatus;
    private final BigDecimal amount;
    private final LocalDateTime createAt;
    private final List<OrderProductDto> orderProducts;

    public static GetReceiptDetailResponse from(Receipt receipt, List<OrderProductDto> orderProducts) {
        return new GetReceiptDetailResponse(
                receipt.getId(),
                receipt.getOrder().getId(),
                receipt.getOrderKey(),
                receipt.getStatus(),
                receipt.getAmount(),
                receipt.getCreatedAt(),
                orderProducts
        );
    }
}
