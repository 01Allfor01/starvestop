package com.allforone.starvestop.domain.payment.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.payment.dto.PurchaseDto;
import com.allforone.starvestop.domain.payment.dto.request.ConfirmPaymentRequest;
import com.allforone.starvestop.domain.payment.dto.request.CreatePaymentRequest;
import com.allforone.starvestop.domain.payment.dto.response.CreatePaymentResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentDetailsResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentResponse;
import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.enums.PurchaseType;
import com.allforone.starvestop.domain.payment.repository.PaymentRepository;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import com.allforone.starvestop.domain.product.repository.ProductRepository;
import com.allforone.starvestop.domain.subscription.entity.Subscription;
import com.allforone.starvestop.domain.subscription.repository.SubscriptionRepository;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.enums.UserRole;
import com.allforone.starvestop.domain.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public CreatePaymentResponse createPayment(
            Long userId,
            CreatePaymentRequest request
    ) {
        String orderId = generateOrderId();

        User user = userRepository.findByIdAndIsDeletedIsFalse(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.getRole().equals(UserRole.USER)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        PurchaseDto purchase = null;
        Long purchaseId = request.getPurchaseId();
        PurchaseType purchaseType = request.getPurchaseType();

        if (purchaseType.equals(PurchaseType.PRODUCT)) {
            Product product = productRepository.findByIdAndIsDeletedIsFalse(purchaseId)
                    .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
            if (product.getStock() <= 0) {
                throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
            }
            purchase = PurchaseDto.of(product.getId(), product.getProductName(), purchaseType, product.getStatus().equals(ProductStatus.GENERAL) ? product.getPrice() : product.getSalePrice());
        } else if (purchaseType.equals(PurchaseType.SUBSCRIPTION)) {
            Subscription subscription = subscriptionRepository.findByIdAndIsDeletedIsFalse(purchaseId)
                    .orElseThrow(() -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
            if (subscription.getStock() <= 0) {
                throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
            }
            purchase = PurchaseDto.of(subscription.getId(), subscription.getSubscriptionName(), purchaseType, subscription.getPrice());
        } else {
            throw new CustomException(ErrorCode.PURCHASE_TYPE_NOT_FOUND);
        }

        Payment payment = Payment.create(
                user,
                purchase.getPurchaseId(),
                purchase.getPurchaseType(),
                purchase.getPurchaseName(),
                orderId,
                purchase.getAmount()
        );

        try {
            paymentRepository.save(payment);
            payment.requestPayment();
            return CreatePaymentResponse.from(payment);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.DUPLICATE_ORDER_ID);
        }
    }

    @Transactional
    public String successPayment(ConfirmPaymentRequest request) throws JsonProcessingException {
        Payment payment = paymentRepository.findPaymentByOrderId(request.getOrderId()).orElseThrow(
                () -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND)
        );

        if (payment.getAmount().compareTo(request.getAmount()) != 0) {
            throw new CustomException(ErrorCode.PAYMENT_VALIDATION_FAILED);
        }

        payment.success(request.getPaymentKey());

        return "/success.html"
                + "?orderId=" + payment.getOrderId()
                + "&amount=" + payment.getAmount();
    }

    @Transactional(readOnly = true)
    public List<GetPaymentResponse> getMyPaymentList(Long userId) {

        List<Payment> paymentList =
                paymentRepository.findByUserIdAndIsDeletedIsFalseOrderByCreatedAtDesc(userId);

        return paymentList.stream().map(GetPaymentResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public GetPaymentDetailsResponse getPayment(Long userId, Long paymentId) {
        Payment payment = paymentRepository.findByIdAndIsDeletedIsFalse(paymentId).orElseThrow(
                () -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

        if (!payment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        return GetPaymentDetailsResponse.from(payment);
    }

    private String generateOrderId() {
        return "PAY_" + UUID.randomUUID().toString().replace("-", "");
    }
}