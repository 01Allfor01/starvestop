package com.allforone.starvestop.domain.payment.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.payment.dto.request.CreatePaymentRequest;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentDetailsResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentResponse;
import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.repository.PaymentRepository;
import com.allforone.starvestop.domain.product.dto.ProductInfo;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.repository.ProductRepository;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.repository.UserRepository;
import com.allforone.starvestop.domain.usersubscription.dto.UserSubscriptionInfo;
import com.allforone.starvestop.domain.usersubscription.entity.UserSubscription;
import com.allforone.starvestop.domain.usersubscription.repository.UserSubscriptionRepository;
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
    private final UserSubscriptionRepository userSubscriptionRepository;

    @Transactional
    public void createPayment(
            Long userId,
            CreatePaymentRequest request
    ) {
        Long productId = request.getProductId();
        Long userSubscriptionId = request.getUserSubscriptionId();
        if (productId == null && userSubscriptionId == null) {
            throw new CustomException(ErrorCode.PAYMENT_TARGET_REQUIRED);
        }
        if (productId != null && userSubscriptionId != null) {
            throw new CustomException(ErrorCode.PAYMENT_TARGET_AMBIGUOUS);
        }

        String orderId = generateOrderId();

        User user = userRepository.getReferenceById(userId);

        Product product = null;
        UserSubscription userSubscription = null;

        if (productId != null) {
            product = productRepository.findById(productId).orElseThrow(
                    () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        } else {
            userSubscription = userSubscriptionRepository.findById(userSubscriptionId).orElseThrow(
                    () -> new CustomException(ErrorCode.USER_SUBSCRIPTION_NOT_FOUND)
            );
        }

        Payment payment = Payment.create(
                user,
                product,
                userSubscription,
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
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

        if (!payment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        UserSubscriptionInfo userSubscriptionInfo = payment.getUserSubscription() != null ? UserSubscriptionInfo.from(
                payment.getUserSubscription(),
                payment.getUserSubscription().getSubscription()) : null;

        ProductInfo productInfo = payment.getProduct() != null ? ProductInfo.from(payment.getProduct()) : null;

        return GetPaymentDetailsResponse.from(payment.getOrderId(), payment.getStatus(), productInfo, userSubscriptionInfo, payment.getCreatedAt());
    }


    private String generateOrderId() {
        return "PAY_" + UUID.randomUUID().toString().replace("-", "");
    }
}