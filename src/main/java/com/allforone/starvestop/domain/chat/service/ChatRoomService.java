package com.allforone.starvestop.domain.chat.service;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.chat.dto.response.ChatRoomResponse;
import com.allforone.starvestop.domain.chat.entity.ChatRoom;
import com.allforone.starvestop.domain.chat.repository.ChatRoomRepository;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.service.StoreService;
import com.allforone.starvestop.domain.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final StoreService storeService;

    @Transactional
    public ChatRoomResponse createChatRoom(AuthUser authUser, Long storeId) {
        Store store = storeService.getById(storeId);
        Long ownerId = store.getOwner().getId();

        if (authUser.getUserRole() != UserRole.USER) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Long userId = authUser.getUserId();

        return chatRoomRepository.findByUserIdAndOwnerId(userId, ownerId)
                .map(ChatRoomResponse::fromForUser)
                .orElseGet(() -> {
                    ChatRoom newRoom = ChatRoom.create(userId, ownerId);
                    ChatRoom savedRoom = chatRoomRepository.save(newRoom);
                    return ChatRoomResponse.fromForUser(savedRoom);
                });
    }

    @Transactional(readOnly = true)
    public Slice<ChatRoomResponse> getChatRoomList(AuthUser authUser, Pageable pageable) {
        if (authUser.getUserRole() == UserRole.USER) {
            return chatRoomRepository.findByUserIdOrderByLastMessageAtDesc(authUser.getUserId(), pageable)
                    .map(ChatRoomResponse::fromForUser);
        } else {
            return chatRoomRepository.findByOwnerIdOrderByLastMessageAtDesc(authUser.getUserId(), pageable)
                    .map(ChatRoomResponse::fromForOwner);
        }
    }

    @Transactional(readOnly = true)
    public ChatRoomResponse getChatRoom(AuthUser authUser, Long roomId) {
        ChatRoom chatRoom = getById(roomId);

        validatePermission(authUser, chatRoom);

        if (authUser.getUserRole() == UserRole.USER) {
            return ChatRoomResponse.fromForUser(chatRoom);
        } else {
            return ChatRoomResponse.fromForOwner(chatRoom);
        }
    }

    private void validatePermission(AuthUser authUser, ChatRoom chatRoom) {
        if (authUser.getUserRole() == UserRole.USER && !chatRoom.getUserId().equals(authUser.getUserId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        if (authUser.getUserRole() == UserRole.OWNER && !chatRoom.getOwnerId().equals(authUser.getUserId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    public ChatRoom getById(Long roomId) {
        return chatRoomRepository.findByIdForUpdate(roomId).orElseThrow(
                () -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND)
        );
    }
}
