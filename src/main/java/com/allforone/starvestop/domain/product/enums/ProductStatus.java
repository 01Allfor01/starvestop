package com.allforone.starvestop.domain.product.enums;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ProductStatus {
    GENERAL,
    SALE,
    ;

    public static ProductStatus from(String status) {
        return Arrays
                .stream(ProductStatus.values())
                .filter(s -> s.name().equals(status))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_STATUS_NOT_FOUND));
    }
}
