package com.allforone.starvestop.domain.product.dto.request;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SalePriceValidator.class)
@Documented
public @interface ValidSalePrice {

    String message() default "세일 가격은 정상 가격보다 저렴해야합니다";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
