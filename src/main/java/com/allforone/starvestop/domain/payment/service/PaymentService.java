package com.allforone.starvestop.domain.payment.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.repository.PaymentRepository;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.usersubscription.entity.UserSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment createPayment(
            User user,
            Product product,
            UserSubscription userSubscription,
            BigDecimal amount
    ) {
        String orderId = generateOrderId();

        Payment payment = Payment.create(
                user,
                product,
                userSubscription,
                orderId,
                amount
        );

        try {
            return paymentRepository.save(payment);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.DUPLICATE_ORDER_ID);
        }
    }

    private String generateOrderId() {
        String PREFIX = "PAY_";
        return PREFIX + UUID.randomUUID().toString().replace("-", "");
    }
}
