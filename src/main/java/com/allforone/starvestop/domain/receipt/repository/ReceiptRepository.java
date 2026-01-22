package com.allforone.starvestop.domain.receipt.repository;

import com.allforone.starvestop.domain.receipt.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findReceiptsByUser_Id(Long userId);
}
