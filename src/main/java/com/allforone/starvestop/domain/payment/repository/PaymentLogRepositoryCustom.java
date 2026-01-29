package com.allforone.starvestop.domain.payment.repository;

import com.allforone.starvestop.domain.payment.dto.response.SearchPaymentLogResponse;

import java.util.List;

public interface PaymentLogRepositoryCustom {
    List<SearchPaymentLogResponse> search(String orderKey, Long userId);
}
