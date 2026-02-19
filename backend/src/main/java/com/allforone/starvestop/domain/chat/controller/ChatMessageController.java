package com.allforone.starvestop.domain.chat.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.chat.dto.request.SendMessageRequest;
import com.allforone.starvestop.domain.chat.dto.response.ChatMessageResponse;
import com.allforone.starvestop.domain.chat.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.allforone.starvestop.common.enums.SuccessMessage.CHAT_MESSAGE_GET_SUCCESS;

@Tag(name = "Chat", description = "채팅 메시지 API")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat-rooms/{roomId}/send")
    public void sendMessage(
            Principal principal,
            @DestinationVariable Long roomId,
            @Payload SendMessageRequest request
    ) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        AuthUser authUser = (AuthUser) token.getPrincipal();
        chatMessageService.sendMessage(roomId, authUser, request);
    }

    @Operation(summary = "채팅 메시지 목록 조회 (USER/OWNER)")
    @GetMapping("/chat-rooms/{roomId}/messages")
    public ResponseEntity<CommonResponse<Slice<ChatMessageResponse>>> getChatMessageList(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long roomId,
            @RequestParam(required = false) Long cursorId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        chatMessageService.markAsRead(authUser, roomId);

        Slice<ChatMessageResponse> responseList = chatMessageService.getChatMessageList(authUser, roomId, cursorId, pageable);

        return ResponseEntity.ok(CommonResponse.success(CHAT_MESSAGE_GET_SUCCESS, responseList));
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<CommonResponse<ChatMessageResponse>> getChatMessage(
            @PathVariable Long messageId
    ) {
        ChatMessageResponse response = chatMessageService.getChatMessage(messageId);

        return ResponseEntity.ok(CommonResponse.success(CHAT_MESSAGE_GET_SUCCESS, response));
    }
}
