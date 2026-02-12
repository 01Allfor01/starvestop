package com.allforone.starvestop.domain.store.service;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.chat.service.ChatRoomService;
import com.allforone.starvestop.domain.store.dto.response.GetStoreForOwnerResponse;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.common.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreForOwnerUseCase {

    private final StoreService storeService;
    private final ChatRoomService chatRoomService;

    @Transactional(readOnly = true)
    public Page<GetStoreForOwnerResponse> getStorePageForOwner(AuthUser authUser, Pageable pageable) {
        if (UserRole.OWNER != authUser.getUserRole()) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        Long ownerId = authUser.getUserId();
        Page<Store> storePage = storeService.getStorePage(ownerId, pageable);

        return storePage.map(store -> {
            Long unreadCount = chatRoomService.countUnread(store.getId());
            return GetStoreForOwnerResponse.from(store, unreadCount);
        });
    }
}
