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
import com.allforone.starvestop.domain.user.enums.UserRole;
import com.allforone.starvestop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        String orderId = generateOrderId();

        User user = userRepository.findByIdAndIsDeletedIsFalse(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.getRole().equals(UserRole.USER)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Long purchaseId = request.getPurchaseId();
        String purchaseName = "";
        PurchaseType purchaseType = request.getPurchaseType();

        if (purchaseType.equals(PurchaseType.PRODUCT)) {
            purchaseName = productRepository.findByIdAndIsDeletedIsFalse(purchaseId)
                    .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND))
                    .getProductName();
        }
        if (purchaseType.equals(PurchaseType.SUBSCRIPTION)) {
            purchaseName = subscriptionRepository.findByIdAndIsDeletedIsFalse(purchaseId)
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
    public Slice<GetPaymentResponse> getMyPaymentList(Long userId, Pageable pageable) {

        Slice<Payment> paymentSlice =
                paymentRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        return paymentSlice.map(GetPaymentResponse::from);
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