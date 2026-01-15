package com.allforone.starvestop.domain.payment.dto.response;

import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.subscription.entity.Subscription;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetPaymentResponse {

    private final String orderId;

    private final Long productId;
    private final String productName;
    private final BigDecimal productPrice;

    private final Long subscriptionId;
    private final String subscriptionName;

    private final PaymentStatus status;
    private final BigDecimal amount;

    public static GetPaymentResponse create(Payment payment) {
        Product product = payment.getProduct();

        Subscription subscription = null;
        if (payment.getUserSubscription() != null) {
            subscription = payment.getUserSubscription().getSubscription();
        }

        return new GetPaymentResponse(
                payment.getOrderId(),

                product != null ? product.getId() : null,
                product != null ? product.getProductName() : null,
                product != null ? product.getPrice() : null,

                subscription != null ? subscription.getId() : null,
                subscription != null ? subscription.getSubscriptionName() : null,

                payment.getStatus(),
                payment.getAmount()
        );
    }
}
