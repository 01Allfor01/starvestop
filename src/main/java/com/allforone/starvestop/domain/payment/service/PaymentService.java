package com.allforone.starvestop.domain.payment.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.order.repository.OrderRepository;
import com.allforone.starvestop.domain.orderproduct.entity.OrderProduct;
import com.allforone.starvestop.domain.orderproduct.repository.OrderProductRepository;
import com.allforone.starvestop.domain.payment.dto.request.ConfirmPaymentRequest;
import com.allforone.starvestop.domain.payment.dto.request.CreatePaymentRequest;
import com.allforone.starvestop.domain.payment.dto.response.CreatePaymentResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentDetailsResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentResponse;
import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.repository.PaymentRepository;
import com.allforone.starvestop.domain.paymentlog.service.PaymentLogService;
import com.allforone.starvestop.domain.product.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final PaymentLogService paymentLogService;

    @Transactional
    public CreatePaymentResponse createPayment(
            Long userId,
            CreatePaymentRequest request
    ) {
        Long orderId = request.getOrderId();

        Order order = orderRepository.findByIdAndIsDeletedIsFalse(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!userId.equals(order.getUser().getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Payment payment = Payment.create(order);
        try {
            paymentRepository.save(payment);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.DUPLICATE_ORDER_ID);
        }

        paymentLogService.savePaymentLog(payment.getId(), payment.getStatus(), null, null);

        payment.requestPayment();
        paymentLogService.savePaymentLog(payment.getId(), payment.getStatus(), null, null);

        return CreatePaymentResponse.from(payment);
    }

    @Transactional
    public String successPayment(ConfirmPaymentRequest request) throws JsonProcessingException {
        Payment payment = paymentRepository.findPaymentByOrderKey(request.getOrderId()).orElseThrow(
                () -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND)
        );

        if (payment.getStatus().equals(PaymentStatus.SUCCEEDED)) {
            paymentLogService.savePaymentLog(payment.getId(), payment.getStatus(), null, null);
            return "/success.html"
                    + "?orderId=" + payment.getOrderKey()
                    + "&amount=" + payment.getAmount();
        }

        if (payment.getStatus().equals(PaymentStatus.FAILED) || payment.getStatus().equals(PaymentStatus.CANCELED)) {
            return "/fail.html"
                    + "?orderId=" + payment.getOrderKey()
                    + "&reason=ALREADY_PROCESSED";
        }

        if (payment.getAmount().compareTo(request.getAmount()) != 0) {
            failAndReleaseReservedStock(payment);

            return "/fail.html"
                    + "?orderId=" + payment.getOrderKey()
                    + "&reason=AMOUNT_MISMATCH";
        }

        payment.success(request.getPaymentKey());
        paymentLogService.savePaymentLog(payment.getId(), payment.getStatus(), null, null);
        return "/success.html"
                + "?orderId=" + payment.getOrderKey()
                + "&amount=" + payment.getAmount();
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


    private void releaseReservedStock(List<OrderProduct> orderProducts) {
        for (OrderProduct orderProduct : orderProducts) {
            productRepository.findByIdAndIsDeletedIsFalse(orderProduct.getProductId()).orElseThrow(
                    () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)
            ).increase(orderProduct.getQuantity());
        }
    }

    private void failAndReleaseReservedStock(Payment payment) {
        if (!(payment.getStatus().equals(PaymentStatus.REQUESTED) || payment.getStatus().equals(PaymentStatus.PENDING))) {
            return;
        }

        payment.fail();
        paymentLogService.savePaymentLog(payment.getId(), payment.getStatus(), null, null);

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder_Id((payment.getOrder().getId()));
        releaseReservedStock(orderProducts);
    }

}
