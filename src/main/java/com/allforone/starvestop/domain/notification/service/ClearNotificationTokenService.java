package com.allforone.starvestop.domain.notification.service;

import com.allforone.starvestop.domain.notification.repository.UserNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClearNotificationTokenService {

    private final UserNotificationRepository userNotificationRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void invalidToken(String token) {

        userNotificationRepository.deleteByToken(token);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void invalidToken(List<String> tokenList) {
        userNotificationRepository.deleteAllByTokenIn(tokenList);
    }
}
