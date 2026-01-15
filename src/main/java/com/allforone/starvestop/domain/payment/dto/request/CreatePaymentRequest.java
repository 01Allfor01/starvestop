package com.allforone.starvestop.domain.payment.dto.request;

import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.usersubscription.entity.UserSubscription;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatePaymentRequest {
    private Product product;
    private UserSubscription userSubscription;
}
