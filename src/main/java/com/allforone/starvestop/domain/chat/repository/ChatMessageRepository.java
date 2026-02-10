package com.allforone.starvestop.domain.chat.repository;

import com.allforone.starvestop.domain.chat.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Slice<ChatMessage> findByRoomIdOrderByIdDesc(Long roomId, Pageable pageable);

    Slice<ChatMessage> findByRoomIdAndIdLessThanOrderByIdDesc(Long roomId, Long cursorId, Pageable pageable);
}
