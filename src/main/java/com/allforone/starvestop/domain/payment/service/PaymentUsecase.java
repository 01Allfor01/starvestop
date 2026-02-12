package com.allforone.starvestop.domain.payment.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.order.entity.OrderProduct;
import com.allforone.starvestop.domain.order.enums.OrderStatus;
import com.allforone.starvestop.domain.order.service.OrderProductService;
import com.allforone.starvestop.domain.order.service.OrderService;
import com.allforone.starvestop.domain.payment.dto.response.CreatePaymentResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentDetailsResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentResponse;
import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.event.PaymentEventRelay;
import com.allforone.starvestop.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentUsecase {

    private final ProductService productService;
    private final OrderService orderService;
    private final OrderProductService orderProductService;
    private final PaymentService paymentService;
    private final PaymentEventRelay paymentEventRelay;

    // 결제 생성
    @Transactional
    public CreatePaymentResponse createPayment(Long userId, Long orderId) {
        Order order = orderService.getForPayment(orderId);

        if (!userId.equals(order.getUser().getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        String orderKey = order.getOrderKey();
        BigDecimal amount = order.getAmount();

        Optional<Payment> existing = paymentService.findByOrderKey(orderKey);
        if (existing.isPresent()) {
            return CreatePaymentResponse.from(existing.get());
        }

        try {
            Payment payment = Payment.request(userId, order, orderKey, amount);
            paymentService.save(payment);

            payment.markRequestedEvent();
            paymentEventRelay.relayFrom(payment);

            return CreatePaymentResponse.from(payment);

        } catch (DataIntegrityViolationException e) {
            Payment found = paymentService.getByOrderKey(orderKey);
            return CreatePaymentResponse.from(found);
        }
    }

    @Transactional
    public String confirmSuccess(String paymentKey, String orderKey, Long amount) {
        Payment payment = paymentService.findByOrderKeyForUpdate(orderKey);
        Order order = orderService.getForPayment(payment.getOrder().getId());

        // 이미 성공
        if (order.getStatus() == OrderStatus.PAID ||
                payment.getStatus() == PaymentStatus.SUCCEEDED) {
            return "/success.html?orderKey=" + payment.getOrderKey()
                    + "&amount=" + payment.getAmount();
        }

        // 진행 중(연타/중복)
        if (payment.getStatus() == PaymentStatus.PENDING) {
            return "/fail.html?orderId=" + payment.getOrderKey()
                    + "&reason=ALREADY_PENDING";
        }

        // 재시도 불가
        if (payment.getStatus() == PaymentStatus.CANCELED ||
                payment.getStatus() == PaymentStatus.FAILED_NON_RETRYABLE) {
            return "/fail.html";
        }

        // 금액 불일치 -> non-retryable + 재고 반환
        if (payment.getAmount().compareTo(BigDecimal.valueOf(amount)) != 0) {
            failAndMaybeReleaseReservedStockExactlyOnce(
                    payment,
                    PaymentStatus.FAILED_NON_RETRYABLE,
                    "PAYMENT_AMOUNT_MISMATCH",
                    paymentService.toJson(Map.of(
                            "code", "PAYMENT_AMOUNT_MISMATCH",
                            "message", "결제 금액이 올바르지 않습니다.",
                            "orderId", orderKey,
                            "paymentKey", paymentKey
                    )),
                    true
            );

            // 실패 이벤트 발행 보장
            paymentEventRelay.relayFrom(payment);

            return "/fail.html?orderId=" + payment.getOrderKey()
                    + "&reason=AMOUNT_MISMATCH";
        }

        // confirm 진행 선점 (REQUESTED/FAILED_RETRYABLE -> PENDING)
        payment.pending();

        Map<String, Object> requestPayload = Map.of(
                "paymentKey", paymentKey,
                "orderId", orderKey,
                "amount", amount
        );

        try {
            paymentService.tossApiConfirm(requestPayload);

            payment.success(paymentKey);
            order.paid();

            // 성공 이벤트(상태변경/영수증) 발행
            paymentEventRelay.relayFrom(payment);

            return "/success.html?orderKey=" + payment.getOrderKey()
                    + "&amount=" + payment.getAmount();

        } catch (WebClientResponseException e) {
            // 8. 실패시 재고 반환
            if (isRetryable(e)) {
                failAndMaybeReleaseReservedStockExactlyOnce(payment, PaymentStatus.FAILED_RETRYABLE, "PG_CONFIRM_FAILED", paymentService.toJson(e), false);
            } else {
                failAndMaybeReleaseReservedStockExactlyOnce(payment, PaymentStatus.FAILED_NON_RETRYABLE, "PG_CONFIRM_FAILED", paymentService.toJson(e), true);
            }


            return "/fail.html?orderId=" + payment.getOrderKey()
                    + "&reason=PG_CONFIRM_FAILED";
        }
    }

    @Transactional(readOnly = true)
    public String failRedirect(String code, String orderId) {
        if (orderId != null) {
            return "/fail.html?orderId=" + orderId + "&reason=" + (code != null ? code : "FAILED");
        }
        return "/fail.html?reason=" + (code != null ? code : "FAILED");
    }

    @Transactional(readOnly = true)
    public Page<GetPaymentResponse> getMyPaymentList(Long userId, Pageable pageable) {

        Page<Payment> paymentList =
                paymentService.findByOrderUserId(userId, pageable);

        return paymentList.map(GetPaymentResponse::from);
    }

    @Transactional(readOnly = true)
    public GetPaymentDetailsResponse getPayment(Long userId, Long paymentId) {
        Payment payment = paymentService.findById(paymentId);

        if (!payment.getOrder().getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        return GetPaymentDetailsResponse.from(payment);
    }

    private boolean isRetryable(WebClientResponseException e) {
        return e.getStatusCode().is5xxServerError();
    }

    private void failAndMaybeReleaseReservedStockExactlyOnce(
            Payment payment,
            PaymentStatus failStatus,
            String pgStatus,
            String payload,
            boolean releaseStock
    ) {
        int claimed = paymentService.checkClaimed(payment);
        if (claimed != 1) {
            return;
        }

        // 1) 상태 확정 + 이벤트 생성
        if (failStatus == PaymentStatus.FAILED_RETRYABLE) {
            payment.failRetryable(pgStatus, payload);
        } else {
            payment.failNonRetryable(pgStatus, payload);
        }

        // 2) 재고 반환(선택) + stockReleased 표시
        if (releaseStock) {
            List<OrderProduct> orderProducts =
                    orderProductService.findListByOrderId(payment.getOrder().getId());

            for (OrderProduct op : orderProducts) {
                productService.increaseById(op.getProductId(), op.getQuantity());
            }

            payment.markStockReleased();
        }
    }

}
