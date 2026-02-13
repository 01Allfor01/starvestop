package com.allforone.starvestop.domain.product.dto.request;

import java.math.BigDecimal;

public interface PriceComparableRequest {
    BigDecimal getPrice();
    BigDecimal getSalePrice();
}
