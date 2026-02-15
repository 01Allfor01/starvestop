package com.allforone.starvestop.domain.store.service;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.common.utils.GeometryUtil;
import com.allforone.starvestop.domain.owner.entity.Owner;
import com.allforone.starvestop.domain.owner.service.OwnerService;
import com.allforone.starvestop.domain.s3.enums.S3BucketStatus;
import com.allforone.starvestop.domain.s3.service.S3Service;
import com.allforone.starvestop.domain.store.dto.StoreDto;
import com.allforone.starvestop.domain.store.dto.StoreRedisDto;
import com.allforone.starvestop.domain.store.dto.condition.SearchStoreCond;
import com.allforone.starvestop.domain.store.dto.request.CreateStoreRequest;
import com.allforone.starvestop.domain.store.dto.request.UpdateStoreRequest;
import com.allforone.starvestop.domain.store.dto.response.CreateStoreResponse;
import com.allforone.starvestop.domain.store.dto.response.GetStoreDetailResponse;
import com.allforone.starvestop.domain.store.dto.response.StoreResponse;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import com.allforone.starvestop.domain.store.repository.StoreRepository;
import com.allforone.starvestop.common.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final S3Service s3Service;
    private final StoreRepository storeRepository;
    private final OwnerService ownerService;
    private final StoreRedisService storeRedisService;

    //매장 추가
    @Transactional
    public CreateStoreResponse createStore(Long ownerId, CreateStoreRequest request) {
        Owner owner = ownerService.getById(ownerId);
        StoreStatus status = getStatus(request.getStatus());
        Point location = getLocation(request.getLongitude(), request.getLatitude());

        Store store = Store.create(
                owner,
                request.getName(),
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

        storeRedisService.create(savedStore.getId(), location.getX(), location.getY());

        return CreateStoreResponse.from(savedStore);
    }

    //매장 정보 수정
    @Transactional
    public CreateStoreResponse updateStore(AuthUser authUser, Long storeId, UpdateStoreRequest request) {
        Store store = getById(storeId);

        idMismatchCheck(authUser, store);

        StoreStatus status = getStatus(request.getStatus());
        Point location = getLocation(request.getLongitude(), request.getLatitude());

        store.update(
                request.getName(),
                request.getAddress(),
                request.getDescription(),
                request.getCategory(),
                location,
                request.getOpenTime(),
                request.getCloseTime(),
                status
        );

        storeRepository.flush();

        storeRedisService.create(store.getId(), location.getX(), location.getY());

        return CreateStoreResponse.from(store);
    }

    //매장 삭제
    @Transactional
    public void deleteStore(AuthUser authUser, Long storeId) {
        Store store = getById(storeId);

        idMismatchCheck(authUser, store);

        store.delete();

        storeRedisService.delete(store.getId());
    }

    //매장 목록 조회
    @Transactional(readOnly = true)
    public Slice<StoreResponse> getStoreSlice(SearchStoreCond cond) {
        double distance = cond.getLastDistance() != null ? cond.getLastDistance() : -1;
        long cursorId = cond.getLastId() != null ? cond.getLastId() : 0L;
        int limitSize = cond.getSize() != null ? cond.getSize() : 10;

        List<StoreRedisDto> storeRedisList = storeRedisService.get(
                cond.getNowLongitude(),
                cond.getNowLatitude(),
                distance,
                cursorId,
                limitSize);

        if (storeRedisList == null || storeRedisList.isEmpty()) {
            Pageable pageable = PageRequest.of(
                    (cond.getSize() == null ? 0 : 1),
                    limitSize
            );

            return new SliceImpl<>(List.of(), pageable, false);
        }

        List<Long> ids = storeRedisList
                .stream()
                .map(StoreRedisDto::getId)
                .toList();

        List<StoreDto> list;

        if (cond.getKeyword() == null) {
            list = storeRepository.findStoreDtoList(ids, cond.getCategory());
        } else {
            list = storeRepository.findWithKeywordStoreDtoList(ids, cond.getKeyword(), cond.getCategory());
        }

        Map<Long, StoreDto> storeMap = list
                .stream()
                .collect(Collectors.toMap(StoreDto::getId, s -> s));

        List<StoreResponse> responseList = storeRedisList
                .stream()
                .filter(redisDto -> storeMap.containsKey(redisDto.getId()))
                .map(redisDto -> {
                    StoreDto storeDto = storeMap.get(redisDto.getId());

                    String imageUrl = s3Service.createPresignedGetUrl(
                            storeDto.getId(),
                            S3BucketStatus.STORE,
                            storeDto.getImageUuid());

                    return StoreResponse.from(
                            storeDto, redisDto,
                            imageUrl
                    );
                })
                .limit(limitSize + 1)
                .collect(Collectors.toList());

        boolean hasNext = responseList.size() > limitSize;

        if (hasNext) {
            responseList.remove(limitSize);
        }

        Pageable pageable = PageRequest.of((cond.getSize() == null ? 0 : 1), limitSize);

        return new SliceImpl<>(responseList, pageable, hasNext);
    }

    //매장 상세 조회
    @Transactional(readOnly = true)
    public GetStoreDetailResponse getStoreDetail(Long storeId) {
        Store store = getById(storeId);

        String imageUrl = s3Service.createPresignedGetUrl(store.getId(), S3BucketStatus.STORE, store.getImageUuid());

        return GetStoreDetailResponse.from(store, imageUrl);
    }

    //매장 확인
    @Transactional
    public Store getById(Long id) {
        return storeRepository.findByIdAndIsDeletedIsFalse(id).orElseThrow(
                () -> new CustomException(ErrorCode.STORE_NOT_FOUND)
        );
    }

    //판매자 아이디 주인 확인
    public void idMismatchCheck(AuthUser authUser, Store store) {
        if (UserRole.ADMIN == authUser.getUserRole()) {
            return;
        }

        if (store.getOwner().getId().equals(authUser.getUserId())) {
            return;
        }

        throw new CustomException(ErrorCode.FORBIDDEN);
    }

    //매장 상태
    private StoreStatus getStatus(StoreStatus status) {
        return status == null ? StoreStatus.CLOSED : status;
    }

    //매장 위치
    private Point getLocation(Double longitude, Double latitude) {
        return GeometryUtil.createPoint(longitude, latitude);
    }

    public Page<Store> getStorePage(Long ownerId, Pageable pageable) {
        return storeRepository.findByOwnerIdAndIsDeletedIsFalseOrderByName(ownerId, pageable);
    }

    public List<Long> findAllActiveStoreIds() {
        return storeRepository.findActiveStoreIds();
    }
}