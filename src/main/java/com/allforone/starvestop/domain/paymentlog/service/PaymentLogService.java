package com.allforone.starvestop.domain.paymentlog.service;

import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.paymentlog.dto.GetPaymentLogResponse;
import com.allforone.starvestop.domain.paymentlog.entity.PaymentLog;
import com.allforone.starvestop.domain.paymentlog.repository.PaymentLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentLogService {

    private final PaymentLogRepository paymentLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void savePaymentLog(Long userId, Long paymentId, String orderKey, PaymentStatus paymentStatus, String pgStatus, String payload) {
        PaymentLog paymentLog = PaymentLog.create(paymentId, userId, orderKey, paymentStatus, pgStatus, payload);
        paymentLogRepository.save(paymentLog);
    }

    @Transactional(readOnly = true)
    public List<GetPaymentLogResponse> getPaymentListByUserId(Long userId) {
        List<PaymentLog> paymentLogList = paymentLogRepository.findByUserId(userId);
        return paymentLogList.stream().map(GetPaymentLogResponse::from).toList();
    }

}
