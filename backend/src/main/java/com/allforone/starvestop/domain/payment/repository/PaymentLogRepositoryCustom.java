package com.allforone.starvestop.domain.payment.repository;

import com.allforone.starvestop.domain.payment.dto.response.SearchPaymentLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentLogRepositoryCustom {
    Page<SearchPaymentLogResponse> search(String orderKey, Long userId, Pageable pageable);
}
