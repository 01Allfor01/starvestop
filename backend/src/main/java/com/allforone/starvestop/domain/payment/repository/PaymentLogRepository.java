package com.allforone.starvestop.domain.payment.repository;

import com.allforone.starvestop.domain.payment.entity.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentLogRepository extends JpaRepository<PaymentLog, Long>, PaymentLogRepositoryCustom {
}
