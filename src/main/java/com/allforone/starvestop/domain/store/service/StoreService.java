package com.allforone.starvestop.domain.store.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.store.dto.StoreRequest;
import com.allforone.starvestop.domain.store.dto.StoreResponse;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
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

        StoreCategory category = StoreCategory.valueOf(request.getCategory());
        StoreStatus status = getStatus(request);
        Point location = getLocation(request);

        Store store = Store.create(
                user,
                request.getStoreName(),
                request.getAddress(),
                request.getDescription(),
                category,
                location,
                request.getOpenTime(),
                request.getCloseTime(),
                status
        );

        Store savedStore = storeRepository.save(store);
        return StoreResponse.from(savedStore);
    }

    @Transactional
    public StoreResponse updateStore(Long userId, Long storeId, StoreRequest request) {
        Store store = getStore(storeId);

        idMismatchCheck(userId, store);

        StoreCategory category = StoreCategory.valueOf(request.getCategory());
        StoreStatus status = getStatus(request);
        Point location = getLocation(request);

        store.update(
                request.getStoreName(),
                request.getAddress(),
                request.getDescription(),
                category,
                location,
                request.getOpenTime(),
                request.getCloseTime(),
                status
        );
        storeRepository.flush();

        return StoreResponse.from(store);
    }

    @Transactional
    public void deleteStore(Long userId, Long storeId) {
        Store store = getStore(storeId);

        idMismatchCheck(userId, store);

        store.delete();
    }

    private Store getStore(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new CustomException(ErrorCode.STORE_NOT_FOUND)
        );
        return store;
    }

    private static void idMismatchCheck(Long userId, Store store) {
        if (!store.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private StoreStatus getStatus(StoreRequest request) {
        StoreStatus status = request.getStatus() == null
                ? StoreStatus.CLOSED : StoreStatus.valueOf(request.getStatus());
        return status;
    }

    private Point getLocation(StoreRequest request) {
        Point location = new Point(
                request.getLongitude(),
                request.getLatitude()
        );
        return location;
    }
}
