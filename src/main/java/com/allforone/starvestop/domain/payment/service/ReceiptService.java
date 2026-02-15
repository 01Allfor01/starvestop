package com.allforone.starvestop.domain.payment.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.order.dto.OrderProductDto;
import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.order.entity.OrderProduct;
import com.allforone.starvestop.domain.order.service.OrderProductService;
import com.allforone.starvestop.domain.payment.dto.response.GetReceiptDetailResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetReceiptResponse;
import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.entity.Receipt;
import com.allforone.starvestop.domain.payment.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final OrderProductService orderProductService;

    // 영수증 생성
    @Transactional
    public void createReceipt(Long userId, Payment payment) {

        Receipt receipt = Receipt.create(userId, payment.getOrder(), payment.getOrderKey(), payment.getPaymentKey(), payment.getAmount());

        receiptRepository.save(receipt);
    }

    // 내 영수증 목록 조회 (N+1 발생 주의 지점 고도화때 해결할 예정)
    @Transactional(readOnly = true)
    public Page<GetReceiptResponse> getReceiptList(Long userId, Pageable pageable) {
        Page<Receipt> receiptList = receiptRepository.findReceiptsByUserId(userId,pageable);

        return receiptList.map((x) -> GetReceiptResponse.from(userId, x));
    }

    // 내 영수증 상세 조회
    @Transactional(readOnly = true)
    public GetReceiptDetailResponse getReceipt(Long userId, Long receiptId) {
        Receipt receipt = receiptRepository.findById(receiptId).orElseThrow(
                () -> new CustomException(ErrorCode.RECEIPT_NOT_FOUND)
        );

        Long orderId = receipt.getOrder().getId();

        if (!userId.equals(receipt.getUserId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        List<OrderProduct> orderList = orderProductService.findListByOrderId(orderId);

        List<OrderProductDto> orderProductDtoList = orderList.stream().map(OrderProductDto::from).toList();

        return GetReceiptDetailResponse.from(receipt, orderProductDtoList);

    }

    @Transactional
    public void save(Long userId, Payment payment) {

        Receipt receipt = Receipt.create(userId, payment.getOrder(), payment.getOrderKey(), payment.getPaymentKey(), payment.getAmount());

        receiptRepository.save(receipt);
    }

    @Transactional
    public void issueIfNotExists(Long userId, Order order, String orderKey, String paymentKey, BigDecimal amount) {
        if (receiptRepository.existsByPaymentKey(paymentKey)) {
            return;
        }
        receiptRepository.save(Receipt.create(userId, order, orderKey, paymentKey, amount));
    }
}
