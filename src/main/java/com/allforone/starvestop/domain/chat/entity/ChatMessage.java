package com.allforone.starvestop.domain.chat.entity;

import com.allforone.starvestop.domain.chat.enums.SenderType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "chat_message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long roomId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SenderType senderType;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ChatMessage(Long roomId, SenderType senderType, Long senderId, String content) {
        this.roomId = roomId;
        this.senderType = senderType;
        this.senderId = senderId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public static ChatMessage create(Long roomId, SenderType senderType, Long senderId, String content) {
        return new ChatMessage(roomId, senderType, senderId, content);
    }
}
