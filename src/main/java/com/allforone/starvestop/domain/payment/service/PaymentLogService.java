package com.allforone.starvestop.domain.payment.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentLogDetailResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentLogResponse;
import com.allforone.starvestop.domain.payment.dto.response.SearchPaymentLogResponse;
import com.allforone.starvestop.domain.payment.entity.PaymentLog;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.repository.PaymentLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentLogService {

    private final PaymentLogRepository paymentLogRepository;

    // 결제로그 전체 조회
    @Transactional(readOnly = true)
    public Page<GetPaymentLogResponse> getPaymentLogResponseList(Pageable pageable) {
        Page<PaymentLog> paymentLogList =
                paymentLogRepository.findAll(pageable);
        return paymentLogList.map(GetPaymentLogResponse::from);
    }

    // 결제로그 상세 조회
    @Transactional(readOnly = true)
    public GetPaymentLogDetailResponse getPaymentLogDetail(Long paymentLogId) {
        PaymentLog paymentLog = paymentLogRepository.findById(paymentLogId).orElseThrow(
                () -> new CustomException(ErrorCode.PAYMENT_LOG_NOT_FOUND)
        );
        return GetPaymentLogDetailResponse.from(paymentLog);
    }

    @Transactional
    public void save(Long paymentId, Long userId, String orderKey, PaymentStatus paymentStatus, String payload) {
        PaymentLog paymentLog = PaymentLog.create(paymentId, userId, orderKey, paymentStatus, payload);
        paymentLogRepository.save(paymentLog);
    }

    @Transactional(readOnly = true)
    public Page<SearchPaymentLogResponse> searchPaymentLog(String orderKey, Long userId, Pageable pageable) {

        return paymentLogRepository.search(orderKey, userId, pageable);
    }
}
