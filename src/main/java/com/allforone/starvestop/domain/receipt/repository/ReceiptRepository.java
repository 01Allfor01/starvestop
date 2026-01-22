package com.allforone.starvestop.domain.receipt.repository;

import com.allforone.starvestop.domain.receipt.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
