package com.allforone.starvestop.domain.notification.controller;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.common.utils.NotificationTokenSet;
import com.allforone.starvestop.domain.notification.dto.NotificationDto;
import com.allforone.starvestop.domain.notification.dto.NotificationMulticastRequest;
import com.allforone.starvestop.domain.notification.NotificationService;
import com.allforone.starvestop.domain.notification.dto.NotificationTokenRequest;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/notifications")
@RestController
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationTokenSet tokens;

    @PostMapping("/save/token")
    public ResponseEntity<CommonResponse<Void>> saveToken(@RequestBody NotificationTokenRequest request) {
        tokens.add(request.getToken());
        return ResponseEntity.ok(CommonResponse.successNoData(SuccessMessage.NOTIFICATION_SEND_SUCCESS));
    }

    @PostMapping("/send/subscription/{subscriptionId}")
    public ResponseEntity<CommonResponse<Map<String, Object>>> sendMultiNotification(@PathVariable Long subscriptionId, @RequestBody NotificationMulticastRequest notification) {
            BatchResponse response = notificationService.sendMultiNotification(subscriptionId, notification, tokens.getAll());

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("response", response.getResponses());
            result.put("success", response.getSuccessCount());
            result.put("failure", response.getFailureCount());

            List<Map<String, String>> failures = new ArrayList<>();
            for (SendResponse r : response.getResponses()) {
                if (!r.isSuccessful()) {
                    failures.add(Map.of("error", r.getException().getMessage()));
                }
            }
            result.put("failures", failures);

            return ResponseEntity.ok(CommonResponse.success(SuccessMessage.NOTIFICATION_SEND_SUCCESS, result));
    }

    @PostMapping("/send")
    public ResponseEntity<CommonResponse<Map<String, Object>>> sendNotification(@RequestBody NotificationDto notification) {
        String response = notificationService.sendNotification(notification);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("response", response);

        return ResponseEntity.ok(CommonResponse.success(SuccessMessage.NOTIFICATION_SEND_SUCCESS, result));
    }
}