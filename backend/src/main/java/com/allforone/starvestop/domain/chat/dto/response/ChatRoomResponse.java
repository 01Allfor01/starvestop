package com.allforone.starvestop.domain.chat.dto.response;

import com.allforone.starvestop.domain.chat.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatRoomResponse {

    private final Long id;
    private final Long userId;
    private final Long ownerId;
    private final Long storeId;
    private final String storeName;
    private final Integer unreadCount;
    private final Long lastMessageId;
    private final LocalDateTime lastMessageAt;

    public static ChatRoomResponse fromForUser(ChatRoom chatRoom) {
        return new ChatRoomResponse(
                chatRoom.getId(),
                chatRoom.getUserId(),
                chatRoom.getOwnerId(),
                chatRoom.getStoreId(),
                chatRoom.getStoreName(),
                chatRoom.getUserUnreadCount(),
                chatRoom.getLastMessageId(),
                chatRoom.getLastMessageAt()
        );
    }

    public static ChatRoomResponse fromForOwner(ChatRoom chatRoom) {
        return new ChatRoomResponse(
                chatRoom.getId(),
                chatRoom.getUserId(),
                chatRoom.getOwnerId(),
                chatRoom.getStoreId(),
                chatRoom.getStoreName(),
                chatRoom.getOwnerUnreadCount(),
                chatRoom.getLastMessageId(),
                chatRoom.getLastMessageAt()
        );
    }
}

