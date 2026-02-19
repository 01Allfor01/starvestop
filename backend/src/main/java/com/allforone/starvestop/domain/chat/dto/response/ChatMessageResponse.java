package com.allforone.starvestop.domain.chat.dto.response;

import com.allforone.starvestop.domain.chat.entity.ChatMessage;
import com.allforone.starvestop.domain.chat.enums.SenderType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatMessageResponse {

    private final Long id;
    private final Long roomId;

    private final SenderType senderType;
    private final Long senderId;

    private final String content;
    private final LocalDateTime createdAt;

    public static ChatMessageResponse from(ChatMessage chatMessage) {
        return new ChatMessageResponse(
                chatMessage.getId(),
                chatMessage.getRoomId(),
                chatMessage.getSenderType(),
                chatMessage.getSenderId(),
                chatMessage.getContent(),
                chatMessage.getCreatedAt()
        );
    }
}
