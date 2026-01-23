package com.allforone.starvestop.domain.payment.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.order.repository.OrderRepository;
import com.allforone.starvestop.domain.orderproduct.entity.OrderProduct;
import com.allforone.starvestop.domain.orderproduct.repository.OrderProductRepository;
import com.allforone.starvestop.domain.payment.dto.response.CreatePaymentResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentDetailsResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentResponse;
import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.repository.PaymentRepository;
import com.allforone.starvestop.domain.paymentlog.service.PaymentLogService;
import com.allforone.starvestop.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final PaymentLogService paymentLogService;

    private final WebClient paymentWebClient;

    // 결제 생성
    @Transactional
    public CreatePaymentResponse createPayment(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndIsDeletedIsFalse(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!userId.equals(order.getUser().getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        String orderKey = order.getOrderKey();
        BigDecimal amount = order.getAmount();

        if (paymentRepository.existsByOrderKey(orderKey)) {
            throw new CustomException(ErrorCode.DUPLICATE_ORDER_ID);
        }

        Payment payment = Payment.create(userId, order, orderKey, amount);

        paymentRepository.save(payment);
        paymentLogService.savePaymentLog(payment.getId(), userId, orderKey, payment.getStatus(), null, null);

        payment.requestPayment();
        paymentLogService.savePaymentLog(payment.getId(), userId, orderKey, payment.getStatus(), null, null);

        return new CreatePaymentResponse(
                orderKey,
                amount
        );
    }

    // 승인 요청
    @Transactional
    public String confirmSuccess(String paymentKey, String orderId, Long amount) {
        Payment payment = paymentRepository.findPaymentByOrderKey(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

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
            paymentLogService.savePaymentLog(payment.getId(), payment.getOrder().getUser().getId(),
                    payment.getOrderKey(), payment.getStatus(), null, null);

            return "/success.html?orderId=" + payment.getOrderKey() + "&amount=" + payment.getAmount();

        } catch (WebClientResponseException e) {
            failAndReleaseReservedStock(payment);
            paymentLogService.savePaymentLog(payment.getId(), payment.getOrder().getUser().getId(),
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
            productRepository.findByIdAndIsDeletedIsFalse(orderProduct.getProductId())
                    .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND))
                    .increase(orderProduct.getQuantity());
        }
    }

    private void failAndReleaseReservedStock(Payment payment) {
        if (!(payment.getStatus() == PaymentStatus.REQUESTED || payment.getStatus() == PaymentStatus.PENDING)) {
            return;
        }
        payment.fail();
        paymentLogService.savePaymentLog(payment.getId(), payment.getOrder().getUser().getId(),
                payment.getOrderKey(), payment.getStatus(), null, null);

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder_Id(payment.getOrder().getId());
        releaseReservedStock(orderProducts);
    }

    @Transactional(readOnly = true)
    public List<GetPaymentResponse> getMyPaymentList(Long userId) {

        List<Payment> paymentList =
                paymentRepository.findAllByOrder_User_IdAndIsDeletedIsFalseOrderByCreatedAtDesc(userId);

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
