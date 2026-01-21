package com.allforone.starvestop.domain.paymentlog.repository;

import com.allforone.starvestop.domain.paymentlog.entity.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentLogRepository extends JpaRepository<PaymentLog, Long> {
}
