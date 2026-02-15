package com.allforone.starvestop.domain.notification.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.docs.ApiRoleLabels;
import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.common.utils.NotificationTokenSet;
import com.allforone.starvestop.domain.notification.dto.NotificationDto;
import com.allforone.starvestop.domain.notification.dto.NotificationMulticastRequest;
import com.allforone.starvestop.domain.notification.service.NotificationJobService;
import com.allforone.starvestop.domain.notification.service.UserNotificationService;
import com.allforone.starvestop.domain.notification.dto.NotificationTokenRequest;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.SendResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Notifications", description = "알림(FCM) API")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class UserNotificationController {

    private final UserNotificationService userNotificationService;
    private final NotificationJobService notificationJobService;
    private final NotificationTokenSet tokens;

    @Operation(summary = "FCM 토큰 저장" + ApiRoleLabels.USER_OWNER)
    @PostMapping("/save/token")
    public ResponseEntity<CommonResponse<Void>> saveToken(@Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser, @RequestBody NotificationTokenRequest request) {
        userNotificationService.saveToken(authUser.getUserId(), authUser.getUserRole(), request.getFcmToken(), request.getPlatform());
        return ResponseEntity.ok(CommonResponse.successNoData(SuccessMessage.NOTIFICATION_SEND_SUCCESS));
    }

    @Operation(summary = "구독자 전체 알림 발송" + ApiRoleLabels.OWNER_ADMIN)
    @PostMapping("/send/subscription/{subscriptionId}")
    public ResponseEntity<CommonResponse<Map<String, Object>>> sendMultiNotification(
            @PathVariable Long subscriptionId, @RequestBody NotificationMulticastRequest notification) {
            BatchResponse response = userNotificationService.sendMultiNotification(subscriptionId, notification, tokens.getAll());

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

    @Operation(summary = "단일 알림 발송" + ApiRoleLabels.OWNER_ADMIN)
    @PostMapping("/send")
    public ResponseEntity<CommonResponse<Map<String, Object>>> sendNotification(@RequestBody NotificationDto notification) {
        String response = userNotificationService.sendNotification(notification);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("response", response);

        return ResponseEntity.ok(CommonResponse.success(SuccessMessage.NOTIFICATION_SEND_SUCCESS, result));
    }

    @PostMapping("/test")
    public void test(@RequestParam Integer bit,@RequestParam Integer bit2) {
        userNotificationService.sendSubscriptionTimeNotification(bit,bit2);
    }

    @PostMapping("/test/scheduler")
    public void testScheduler() {
        notificationJobService.preload();
    }
}