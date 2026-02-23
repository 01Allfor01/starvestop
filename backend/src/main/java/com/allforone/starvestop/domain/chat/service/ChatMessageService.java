package com.allforone.starvestop.domain.chat.service;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.enums.UserRole;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.chat.dto.request.SendMessageRequest;
import com.allforone.starvestop.domain.chat.dto.response.ChatMessageResponse;
import com.allforone.starvestop.domain.chat.entity.ChatMessage;
import com.allforone.starvestop.domain.chat.entity.ChatRoom;
import com.allforone.starvestop.domain.chat.enums.SenderType;
import com.allforone.starvestop.domain.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void sendMessage(Long roomId, AuthUser authUser, SendMessageRequest request) {
        ChatRoom chatRoom = chatRoomService.getById(roomId);
        checkPermission(authUser, chatRoom);

        SenderType senderType;
        if (authUser.getUserRole() == UserRole.USER) {
            if (!chatRoom.getUserId().equals(authUser.getUserId())) {
                throw new CustomException(ErrorCode.FORBIDDEN);
            }
            senderType = SenderType.USER;
        } else {
            if (!chatRoom.getOwnerId().equals(authUser.getUserId())) {
                throw new CustomException(ErrorCode.FORBIDDEN);
            }
            senderType = SenderType.OWNER;
        }

        ChatMessage chatMessage = ChatMessage.create(
                roomId,
                senderType,
                authUser.getUserId(),
                request.getContent()
        );
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        chatRoom.updateLastMessage(savedMessage.getId(), savedMessage.getCreatedAt());

        if (senderType == SenderType.USER) {
            chatRoom.increaseOwnerUnreadCount();
        } else {
            chatRoom.increaseUserUnreadCount();
        }

        ChatMessageResponse response = ChatMessageResponse.from(savedMessage);
        messagingTemplate.convertAndSend("/sub/chat-rooms/" + roomId, response);
    }

    @Transactional
    public Slice<ChatMessageResponse> getChatMessageList(AuthUser authUser, Long roomId, Long cursorId, Pageable pageable) {
        ChatRoom chatRoom = chatRoomService.getById(roomId);
        checkPermission(authUser, chatRoom);
        Slice<ChatMessage> messages;

        if (cursorId == null) {
            messages = chatMessageRepository.findByRoomIdOrderByIdDesc(roomId, pageable);
        } else {
            messages = chatMessageRepository.findByRoomIdAndIdLessThanOrderByIdDesc(roomId, cursorId, pageable);
        }

        return messages.map(ChatMessageResponse::from);
    }

    @Transactional
    public void markAsRead(AuthUser authUser, Long roomId) {
        ChatRoom chatRoom = chatRoomService.getById(roomId);
        checkPermission(authUser, chatRoom);

        if (authUser.getUserRole() == UserRole.USER) {
            chatRoom.resetUserUnreadCount();
        } else if (authUser.getUserRole() == UserRole.OWNER) {
            chatRoom.resetOwnerUnreadCount();
        }
    }

    private void checkPermission(AuthUser authUser, ChatRoom chatRoom) {
        if (!chatRoom.getUserId().equals(authUser.getUserId()) && !chatRoom.getOwnerId().equals(authUser.getUserId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    public ChatMessageResponse getChatMessage(Long messageId) {
        return ChatMessageResponse.from(chatMessageRepository.findById(messageId).orElseThrow(
                () -> new CustomException(ErrorCode.CHAT_MESSAGE_NOT_FOUND)
        ));
    }
}
