package com.allforone.starvestop.domain.payment.service;

import com.allforone.starvestop.domain.payment.entity.PaymentLog;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.repository.PaymentLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentLogFunction {

    private final PaymentLogRepository paymentLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(Long paymentId, Long userId, String orderKey, PaymentStatus paymentStatus, String pgStatus, String payload) {
        PaymentLog paymentLog = PaymentLog.create(paymentId, userId, orderKey, paymentStatus, pgStatus, payload);
        paymentLogRepository.save(paymentLog);
    }

}
