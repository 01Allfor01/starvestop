package com.allforone.starvestop.domain.store.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.common.utils.GeometryUtil;
import com.allforone.starvestop.domain.owner.entity.Owner;
import com.allforone.starvestop.domain.owner.service.OwnerFunction;
import com.allforone.starvestop.domain.store.dto.condition.SearchStoreCond;
import com.allforone.starvestop.domain.store.dto.request.CreateStoreRequest;
import com.allforone.starvestop.domain.store.dto.request.UpdateStoreRequest;
import com.allforone.starvestop.domain.store.dto.response.StoreDetailResponse;
import com.allforone.starvestop.domain.store.dto.response.StoreListResponse;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import com.allforone.starvestop.domain.store.repository.StoreRepository;
import com.allforone.starvestop.domain.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final OwnerFunction ownerFunction;

    //매장 추가
    @Transactional
    public StoreDetailResponse createStore(CreateStoreRequest request) {
        Owner owner = ownerFunction.getById(request.getOwnerId());

        if (!UserRole.OWNER.equals(owner.getRole())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        StoreStatus status = getStatus(request.getStatus());
        Point location = getLocation(request.getLongitude(), request.getLatitude());

        Store store = Store.create(
                owner,
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
        return StoreDetailResponse.from(savedStore);
    }

    //매장 정보 수정
    @Transactional
    public StoreDetailResponse updateStore(Long userId, Long storeId, UpdateStoreRequest request) {
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

        return StoreDetailResponse.from(store);
    }

    @Transactional
    public void deleteStore(Long userId, Long storeId) {
        Store store = getStore(storeId);

        idMismatchCheck(userId, store);

        store.delete();
    }

    //매장 목록 조회
    @Transactional(readOnly = true)
    public List<StoreListResponse> getStoreList(SearchStoreCond cond) {
        return storeRepository.searchStoreList(cond);
    }

    //매장 상세 조회
    @Transactional(readOnly = true)
    public StoreDetailResponse getStoreDetail(Long storeId) {
        Store store = getStore(storeId);

        return StoreDetailResponse.from(store);
    }

    //매장 확인
    @Transactional
    public Store getStore(Long storeId) {
        return storeRepository.findByIdAndIsDeletedIsFalse(storeId).orElseThrow(
                () -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

    //판매자 아이디 주인 확인
    public void idMismatchCheck(Long ownerId, Store store) {
        if (!store.getOwner().getId().equals(ownerId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    //매장 상태
    private StoreStatus getStatus(StoreStatus status) {
        return status == null ? StoreStatus.CLOSED : status;
    }

    //매장
    private Point getLocation(Double longitude, Double latitude) {
        return GeometryUtil.createPoint(longitude, latitude);
    }
}
