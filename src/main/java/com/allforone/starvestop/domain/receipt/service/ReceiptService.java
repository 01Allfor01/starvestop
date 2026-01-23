package com.allforone.starvestop.domain.receipt.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.orderproduct.dto.OrderProductDto;
import com.allforone.starvestop.domain.orderproduct.entity.OrderProduct;
import com.allforone.starvestop.domain.orderproduct.repository.OrderProductRepository;
import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.receipt.dto.response.GetReceiptDetailResponse;
import com.allforone.starvestop.domain.receipt.dto.response.GetReceiptResponse;
import com.allforone.starvestop.domain.receipt.entity.Receipt;
import com.allforone.starvestop.domain.receipt.repository.ReceiptRepository;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final UserRepository userRepository;
    private final OrderProductRepository orderProductRepository;

    // 영수증 생성
    @Transactional
    public void createReceipt(Long userId, Payment payment) {

        User user = userRepository.findByIdAndIsDeletedIsFalse(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Receipt receipt = Receipt.create(user, payment.getOrder(), payment.getOrderKey(), payment.getPaymentKey(), payment.getAmount());

        receiptRepository.save(receipt);
    }

    // 내 영수증 목록 조회 (N+1 발생 주의 지점 고도화때 해결할 예정)
    @Transactional(readOnly = true)
    public List<GetReceiptResponse> getReceiptList(Long userId) {
        List<Receipt> receiptList = receiptRepository.findReceiptsByUserId(userId);

        return receiptList.stream().map((x) -> GetReceiptResponse.from(userId, x)).toList();
    }

    // 내 영수증 상세 조회
    @Transactional(readOnly = true)
    public GetReceiptDetailResponse getReceipt(Long userId, Long receiptId) {
        Receipt receipt = receiptRepository.findById(receiptId).orElseThrow(
                () -> new CustomException(ErrorCode.RECEIPT_NOT_FOUND)
        );

        Long orderId = receipt.getOrder().getId();
        Long receiptOwnerId = receipt.getUser().getId();

        if (userId.equals(receiptOwnerId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        List<OrderProduct> orderList = orderProductRepository.findAllByOrder_Id(orderId);

        List<OrderProductDto> orderProductDtoList = orderList.stream().map(OrderProductDto::from).toList();

        return GetReceiptDetailResponse.from(receipt, orderProductDtoList);

    }
    // 환불 요청

}
