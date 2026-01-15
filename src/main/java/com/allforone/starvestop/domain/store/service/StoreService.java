package com.allforone.starvestop.domain.store.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.store.dto.StoreRequest;
import com.allforone.starvestop.domain.store.dto.StoreResponse;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.repository.StoreRepository;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public StoreResponse createStore(Long userId, StoreRequest request) {
        User user = userRepository.getReferenceById(userId);

        Point location = new Point(
                request.getLongitude(),
                request.getLatitude()
        );

        Store store = Store.create(
                user,
                request.getStoreName(),
                request.getAddress(),
                request.getDescription(),
                request.getCategory(),
                location,
                request.getOpenTime(),
                request.getCloseTime()
        );

        Store savedStore = storeRepository.save(store);
        return StoreResponse.from(savedStore);
    }

    public StoreResponse updateStore(Long userId, Long storeId, StoreRequest request) {
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new CustomException(ErrorCode.STORE_NOT_FOUND)
        );

        if (!store.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Point location = new Point(
                request.getLongitude(),
                request.getLatitude()
        );

        store.update(
                request.getStoreName(),
                request.getAddress(),
                request.getDescription(),
                request.getCategory(),
                location,
                request.getOpenTime(),
                request.getCloseTime()
        );
        storeRepository.flush();

        return StoreResponse.from(store);
    }
}
