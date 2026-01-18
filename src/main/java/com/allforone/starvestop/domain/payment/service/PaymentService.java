package com.allforone.starvestop.domain.payment.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.payment.dto.request.CreatePaymentRequest;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentDetailsResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentResponse;
import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.enums.PurchaseType;
import com.allforone.starvestop.domain.payment.repository.PaymentRepository;
import com.allforone.starvestop.domain.product.repository.ProductRepository;
import com.allforone.starvestop.domain.subscription.repository.SubscriptionRepository;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public void createPayment(
            Long userId,
            CreatePaymentRequest request
    ) {
        Long productId = request.getProductId();
        Long subscriptionId = request.getSubscriptionId();
        if (productId == null && subscriptionId == null) {
            throw new CustomException(ErrorCode.PAYMENT_TARGET_REQUIRED);
        }
        if (productId != null && subscriptionId != null) {
            throw new CustomException(ErrorCode.PAYMENT_TARGET_AMBIGUOUS);
        }

        String orderId = generateOrderId();

        User user = userRepository.getReferenceById(userId);

        Long purchaseId;
        String purchaseName;
        PurchaseType purchaseType;

        if (productId != null) {
            purchaseId = productId;
            purchaseType = PurchaseType.PRODUCT;
            purchaseName = productRepository.findByIdAndIsDeletedIsFalse(productId)
                    .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND))
                    .getProductName();
        } else {
            purchaseId = subscriptionId;
            purchaseType = PurchaseType.SUBSCRIPTION;
            purchaseName = subscriptionRepository.findByIdAndIsDeletedIsFalse(subscriptionId)
                    .orElseThrow(() -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND))
                    .getSubscriptionName();
        }

        Payment payment = Payment.create(
                user,
                purchaseId,
                purchaseType,
                purchaseName,
                orderId,
                request.getAmount()
        );

        try {
            paymentRepository.save(payment);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.DUPLICATE_ORDER_ID);
        }
    }

    @Transactional(readOnly = true)
    public List<GetPaymentResponse> getMyPaymentList(Long userId) {
        List<Payment> paymentList = paymentRepository.getPaymentsByUserId(userId);

        return paymentList.stream().map(GetPaymentResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public GetPaymentDetailsResponse getPayment(Long userId, Long paymentId) {
        Payment payment = paymentRepository.findByIdAndIsDeletedIsFalse(paymentId).orElseThrow(
                () -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

        if (!payment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        return GetPaymentDetailsResponse.from(
                payment.getOrderId(),
                payment.getStatus(),
                payment.getPurchaseType(),
                payment.getPurchaseId(),
                payment.getPurchaseName(),
                payment.getCreatedAt());
    }

    private String generateOrderId() {
        return "PAY_" + UUID.randomUUID().toString().replace("-", "");
    }
}