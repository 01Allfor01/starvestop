package com.allforone.starvestop.domain.chat.repository;

import com.allforone.starvestop.domain.chat.entity.ChatRoom;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from ChatRoom r where r.id = :roomId")
    Optional<ChatRoom> findByIdForUpdate(@Param("roomId") Long roomId);

    Slice<ChatRoom> findByUserIdOrderByLastMessageAtDesc(Long userId, Pageable pageable);

    Slice<ChatRoom> findByOwnerIdOrderByLastMessageAtDesc(Long ownerId, Pageable pageable);

    Optional<ChatRoom> findByUserIdAndOwnerId(Long userId, Long ownerId);

    @Query("SELECT COALESCE(SUM(c.ownerUnreadCount), 0) FROM ChatRoom c WHERE c.storeId = :storeId")
    Long countTotalOwnerUnreadByStoreId(@Param("storeId") Long storeId);
}
