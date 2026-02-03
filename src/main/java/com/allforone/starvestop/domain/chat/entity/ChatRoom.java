package com.allforone.starvestop.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "chat_room",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_chat_room_user_owner",
                        columnNames = {"user_id", "owner_id"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private int userUnreadCount;

    @Column(nullable = false)
    private int ownerUnreadCount;

    @Column
    private Long lastMessageId;

    @Column(nullable = false)
    private LocalDateTime lastMessageAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ChatRoom(Long userId, Long ownerId) {
        this.userId = userId;
        this.ownerId = ownerId;
        this.userUnreadCount = 0;
        this.ownerUnreadCount = 0;
        this.createdAt = LocalDateTime.now();
        this.lastMessageAt = this.createdAt;
    }

    public static ChatRoom create(Long userId, Long ownerId) {
        return new ChatRoom(userId, ownerId);
    }

    public void updateLastMessage(Long messageId, LocalDateTime messageTime) {
        this.lastMessageId = messageId;
        this.lastMessageAt = messageTime;
    }

    public void increaseUserUnreadCount() {
        this.userUnreadCount++;
    }

    public void increaseOwnerUnreadCount() {
        this.ownerUnreadCount++;
    }

    public void resetUserUnreadCount() {
        this.userUnreadCount = 0;
    }

    public void resetOwnerUnreadCount() {
        this.ownerUnreadCount = 0;
    }
}
