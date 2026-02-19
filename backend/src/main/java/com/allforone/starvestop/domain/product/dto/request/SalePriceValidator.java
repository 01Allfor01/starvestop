package com.allforone.starvestop.domain.product.dto.request;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SalePriceValidator implements ConstraintValidator<ValidSalePrice, PriceComparableRequest> {

    @Override
    public boolean isValid(PriceComparableRequest request, ConstraintValidatorContext context) {
        if (request.getPrice() == null || request.getSalePrice() == null) {
            return true;
        }

        if (request.getSalePrice().compareTo(request.getPrice()) > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("세일 가격은 정상 가격보다 저렴해야 합니다")
                    .addPropertyNode("salePrice")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
