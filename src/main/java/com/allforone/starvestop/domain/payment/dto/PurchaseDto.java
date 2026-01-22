package com.allforone.starvestop.domain.payment.dto;

import com.allforone.starvestop.domain.payment.enums.PurchaseType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PurchaseDto {
    private Long purchaseId;
    private String purchaseName;
    private PurchaseType purchaseType;
    private BigDecimal amount;

    public static PurchaseDto of(Long purchaseId, String purchaseName, PurchaseType purchaseType, BigDecimal amount) {
        return new PurchaseDto(
                purchaseId,
                purchaseName,
                purchaseType,
                amount
        );
    }
}
