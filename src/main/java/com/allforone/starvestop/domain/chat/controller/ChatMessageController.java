package com.allforone.starvestop.domain.chat.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.chat.dto.request.SendMessageRequest;
import com.allforone.starvestop.domain.chat.dto.response.ChatMessageResponse;
import com.allforone.starvestop.domain.chat.service.ChatMessageService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.allforone.starvestop.common.enums.SuccessMessage.CHAT_MESSAGE_GET_SUCCESS;

@RestController
@RequiredArgsConstructor
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
}
