package com.allforone.starvestop.domain.notification.service;

import com.allforone.starvestop.domain.notification.repository.UserNotificationRepository;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MessagingErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClearNotificationTokenService {

    private final UserNotificationRepository userNotificationRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void invalidToken(String token, FirebaseMessagingException e) {
        MessagingErrorCode code = e.getMessagingErrorCode();

        if (code.equals(MessagingErrorCode.UNREGISTERED)
                || code.equals(MessagingErrorCode.INVALID_ARGUMENT)) {
            userNotificationRepository.deleteByToken(token);
        }
    }
}
