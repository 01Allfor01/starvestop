package com.allforone.starvestop.domain.payment.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.order.entity.OrderProduct;
import com.allforone.starvestop.domain.order.enums.OrderStatus;
import com.allforone.starvestop.domain.order.service.OrderFunction;
import com.allforone.starvestop.domain.order.service.OrderProductFunction;
import com.allforone.starvestop.domain.payment.dto.response.CreatePaymentResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentDetailsResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentResponse;
import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.repository.PaymentRepository;
import com.allforone.starvestop.domain.product.service.ProductFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentLogFunction paymentLogFunction;
    private final ReceiptFunction receiptFunction;
    private final PaymentRepository paymentRepository;
    private final ProductFunction productFunction;
    private final OrderFunction orderFunction;
    private final OrderProductFunction orderProductFunction;

    private final WebClient paymentWebClient;

    private final String baseUrl = "http://localhost:8080/";

    // 결제 생성
    @Transactional
    public CreatePaymentResponse createPayment(Long userId, Long orderId) {
        Order order = orderFunction.getForPayment(orderId);
        // 1. 같은 유저인지 확인
        if (!userId.equals(order.getUser().getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        // 2.
        String orderKey = order.getOrderKey();
        BigDecimal amount = order.getAmount();

        Optional<Payment> existing = paymentRepository.findPaymentByOrderKey(orderKey);
        if (existing.isPresent()) {
            return CreatePaymentResponse.from(existing.get());
        }

        try {
            Payment payment = Payment.create(userId, order, orderKey, amount);
            paymentRepository.save(payment);

            paymentLogFunction.save(
                    payment.getId(),
                    userId,
                    orderKey,
                    payment.getStatus(),
                    null,
                    null
            );

            payment.requestPayment();

            paymentLogFunction.save(
                    payment.getId(),
                    userId,
                    orderKey,
                    payment.getStatus(),
                    null,
                    null
            );

            return CreatePaymentResponse.from(payment);

        } catch (DataIntegrityViolationException e) {
            Payment found = paymentRepository.getPaymentByOrderKey(orderKey);
            return CreatePaymentResponse.from(found);
        }
    }

    // 승인 요청
    @Transactional
    public String confirmSuccess(String paymentKey, String orderId, Long amount) {
        Payment payment = paymentRepository.findPaymentByOrderKey(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
        Order order = orderFunction.getForPayment(payment.getOrder().getId());

        if (order.getStatus().equals(OrderStatus.PAID) || payment.getStatus().equals(PaymentStatus.SUCCEEDED)) {
            return "/success.html?orderKey=" + payment.getOrderKey() + "&amount=" + payment.getAmount();
        }

        if (payment.getAmount().compareTo(BigDecimal.valueOf(amount)) != 0) {
            failAndReleaseReservedStock(payment);
            return "/fail.html?orderId=" + payment.getOrderKey() + "&reason=AMOUNT_MISMATCH";
        }

        try {
            paymentWebClient.post()
                    .uri("/v1/payments/confirm")
                    .bodyValue(Map.of(
                            "paymentKey", paymentKey,
                            "orderId", orderId,
                            "amount", amount
                    ))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            payment.success(paymentKey);
            receiptFunction.save(payment.getUserId(), payment);
            paymentLogFunction.save(payment.getOrder().getUser().getId(), payment.getId(),
                    payment.getOrderKey(), payment.getStatus(), null, null);

            order.setStatus(OrderStatus.PAID);
            return "/success.html?orderKey=" + payment.getOrderKey() + "&amount=" + payment.getAmount();

        } catch (WebClientResponseException e) {
            failAndReleaseReservedStock(payment);
            paymentLogFunction.save(payment.getOrder().getUser().getId(), payment.getId(),
                    payment.getOrderKey(), payment.getStatus(), String.valueOf(e.getStatusCode()), e.getResponseBodyAsString());

            return "/fail.html?orderId=" + payment.getOrderKey() + "&reason=PG_CONFIRM_FAILED";
        }
    }

    @Transactional(readOnly = true)
    public String failRedirect(String code, String orderId) {
        if (orderId != null) {
            return "/fail.html?orderId=" + orderId + "&reason=" + (code != null ? code : "FAILED");
        }
        return "/fail.html?reason=" + (code != null ? code : "FAILED");
    }

    private void releaseReservedStock(List<OrderProduct> orderProducts) {
        for (OrderProduct orderProduct : orderProducts) {
            productFunction.increaseById(orderProduct.getProductId(), orderProduct.getQuantity());
        }
    }

    private void failAndReleaseReservedStock(Payment payment) {
        if (!(payment.getStatus() == PaymentStatus.REQUESTED || payment.getStatus() == PaymentStatus.PENDING)) {
            return;
        }
        payment.fail();
        paymentLogFunction.save(payment.getId(), payment.getOrder().getUser().getId(),
                payment.getOrderKey(), payment.getStatus(), null, null);

        List<OrderProduct> orderProducts = orderProductFunction.findListByOrderId(payment.getOrder().getId());
        releaseReservedStock(orderProducts);
    }

    @Transactional(readOnly = true)
    public List<GetPaymentResponse> getMyPaymentList(Long userId) {

        List<Payment> paymentList =
                paymentRepository.findAllByOrderUserIdAndIsDeletedIsFalseOrderByCreatedAtDesc(userId);

        return paymentList.stream().map(GetPaymentResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public GetPaymentDetailsResponse getPayment(Long userId, Long paymentId) {
        Payment payment = paymentRepository.findByIdAndIsDeletedIsFalse(paymentId).orElseThrow(
                () -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

        if (!payment.getOrder().getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        return GetPaymentDetailsResponse.from(payment);
    }
}
