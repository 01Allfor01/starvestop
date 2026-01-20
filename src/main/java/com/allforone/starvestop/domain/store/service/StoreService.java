package com.allforone.starvestop.domain.store.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.store.dto.condition.SearchStoreCond;
import com.allforone.starvestop.domain.store.dto.request.StoreRequest;
import com.allforone.starvestop.domain.store.dto.request.UpdateStoreRequest;
import com.allforone.starvestop.domain.store.dto.response.StoreListResponse;
import com.allforone.starvestop.domain.store.dto.response.StoreResponse;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import com.allforone.starvestop.domain.store.repository.StoreRepository;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.enums.UserRole;
import com.allforone.starvestop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public StoreResponse createStore(StoreRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        if (!UserRole.OWNER.equals(user.getRole())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        StoreStatus status = getStatus(request.getStatus());
        Point location = getLocation(request.getLongitude(), request.getLatitude());

        Store store = Store.create(
                user,
                request.getStoreName(),
                request.getAddress(),
                request.getDescription(),
                request.getCategory(),
                location,
                request.getOpenTime(),
                request.getCloseTime(),
                status,
                request.getBusinessRegistrationNumber()
        );

        Store savedStore = storeRepository.save(store);
        return StoreResponse.from(savedStore);
    }

    @Transactional
    public StoreResponse updateStore(Long userId, Long storeId, UpdateStoreRequest request) {
        Store store = getStore(storeId);

        idMismatchCheck(userId, store);

        StoreStatus status = getStatus(request.getStatus());
        Point location = getLocation(request.getLongitude(), request.getLatitude());

        store.update(
                request.getStoreName(),
                request.getAddress(),
                request.getDescription(),
                request.getCategory(),
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

    @Transactional(readOnly = true)
    public StoreResponse getStoreDetail(Long storeId) {
        Store store = getStore(storeId);
        return StoreResponse.from(store);
    }

    @Transactional(readOnly = true)
    public List<StoreListResponse> getStoreList(SearchStoreCond cond) {
        return storeRepository.searchStoreList(cond);
    }

    private Store getStore(Long storeId) {
        return storeRepository.findByIdAndIsDeletedIsFalse(storeId).orElseThrow(
                () -> new CustomException(ErrorCode.STORE_NOT_FOUND)
        );
    }

    private static void idMismatchCheck(Long userId, Store store) {
        if (!store.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private StoreStatus getStatus(StoreStatus status) {
        return status == null
                ? StoreStatus.CLOSED : status;
    }

    private Point getLocation(Double longitude, Double latitude) {
        return  new Point(
                longitude,
                latitude
        );
    }
}
