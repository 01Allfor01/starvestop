package com.allforone.starvestop.domain.paymentlog.service;

import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.paymentlog.entity.PaymentLog;
import com.allforone.starvestop.domain.paymentlog.repository.PaymentLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentLogService {

    private final PaymentLogRepository paymentLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void savePaymentLog(Long paymentId, PaymentStatus paymentStatus, String pgStatus, String payload) {
        PaymentLog paymentLog = PaymentLog.create(paymentId, paymentStatus, pgStatus, payload);
        paymentLogRepository.save(paymentLog);
    }


}
