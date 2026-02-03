package com.allforone.starvestop.domain.notification;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.common.utils.NotificationTokenSet;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/send")
    public ResponseEntity<CommonResponse<Map<String, Object>>> sendMultiNotification(@RequestBody NotificationDto notification) {
            BatchResponse response = notificationService.sendMultiNotification(notification, tokens.getAll());

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

//    @PostMapping("/send")
//    public ResponseEntity<CommonResponse<Map<String, Object>>> sendNotification(@RequestBody NotificationDto notification) {
//        BatchResponse response = notificationService.sendNotification(notification);
//
//        Map<String, Object> result = new LinkedHashMap<>();
//        result.put("response", response.getResponses());
//        result.put("success", response.getSuccessCount());
//        result.put("failure", response.getFailureCount());
//
//        List<Map<String, String>> failures = new ArrayList<>();
//        for (SendResponse r : response.getResponses()) {
//            if (!r.isSuccessful()) {
//                failures.add(Map.of("error", r.getException().getMessage()));
//            }
//        }
//        result.put("failures", failures);
//
//        return ResponseEntity.ok(CommonResponse.success(SuccessMessage.NOTIFICATION_SEND_SUCCESS, result));
//    }
}