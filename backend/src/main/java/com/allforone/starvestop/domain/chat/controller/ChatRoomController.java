package com.allforone.starvestop.domain.chat.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.chat.dto.response.ChatRoomResponse;
import com.allforone.starvestop.domain.chat.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.allforone.starvestop.common.enums.SuccessMessage.CHAT_ROOM_CREATE_SUCCESS;
import static com.allforone.starvestop.common.enums.SuccessMessage.CHAT_ROOM_GET_SUCCESS;

@Tag(name = "Chat", description = "채팅방 API")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Operation(summary = "채팅방 생성 (USER)")
    @PostMapping("/stores/{storeId}/chat-rooms")
    public ResponseEntity<CommonResponse<ChatRoomResponse>> createChatRoom(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long storeId
    ) {
        ChatRoomResponse response = chatRoomService.createChatRoom(authUser, storeId);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(CHAT_ROOM_CREATE_SUCCESS, response));
    }

    @Operation(summary = "채팅방 목록 조회 (USER/OWNER)")
    @GetMapping("/chat-rooms")
    public ResponseEntity<CommonResponse<Slice<ChatRoomResponse>>> getChatRoomList(
            @AuthenticationPrincipal AuthUser authUser,
            @PageableDefault(sort = "lastMessageAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Slice<ChatRoomResponse> responseList = chatRoomService.getChatRoomList(authUser, pageable);
        return ResponseEntity.ok(CommonResponse.success(CHAT_ROOM_GET_SUCCESS, responseList));
    }

    @Operation(summary = "채팅방 상세 조회 (USER/OWNER)")
    @GetMapping("/chat-rooms/{roomId}")
    public ResponseEntity<CommonResponse<ChatRoomResponse>> getChatRoom(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long roomId
    ) {
        ChatRoomResponse response = chatRoomService.getChatRoom(authUser, roomId);
        return ResponseEntity.ok(CommonResponse.success(CHAT_ROOM_GET_SUCCESS, response));
    }
}
