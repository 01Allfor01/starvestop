package com.allforone.starvestop.domain.payment.service;

import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.entity.Receipt;
import com.allforone.starvestop.domain.payment.repository.ReceiptRepository;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReceiptFunction {

    private final ReceiptRepository receiptRepository;
    private final UserService userService;

    @Transactional
    public void save(Long userId, Payment payment) {

        User user = userService.getById(userId);

        Receipt receipt = Receipt.create(user, payment.getOrder(), payment.getOrderKey(), payment.getPaymentKey(), payment.getAmount());

        receiptRepository.save(receipt);
    }
}
