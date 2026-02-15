package com.allforone.starvestop.domain.subscription.service;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.store.dto.StoreRedisDto;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.service.StoreRedisService;
import com.allforone.starvestop.domain.store.service.StoreService;
import com.allforone.starvestop.domain.subscription.dto.SubscriptionDto;
import com.allforone.starvestop.domain.subscription.dto.condition.SearchSubscriptionCond;
import com.allforone.starvestop.domain.subscription.dto.request.CreateSubscriptionRequest;
import com.allforone.starvestop.domain.subscription.dto.request.UpdateSubscriptionRequest;
import com.allforone.starvestop.domain.subscription.dto.response.CreateSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.dto.response.GetSubscriptionDistanceResponse;
import com.allforone.starvestop.domain.subscription.dto.response.GetSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.dto.response.UpdateSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.entity.Subscription;
import com.allforone.starvestop.domain.subscription.repository.SubscriptionRepository;
import com.allforone.starvestop.common.enums.UserRole;
import jakarta.validation.Valid;
import org.springframework.data.domain.Slice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final StoreService storeService;
    private final StoreRedisService storeRedisService;

    // 구독 생성
    @Transactional
    public CreateSubscriptionResponse createSubscription(AuthUser authUser, Long storeId, @Valid CreateSubscriptionRequest request) {
        Store store = storeService.getById(storeId);

        Long ownerId = authUser.getUserId();
        if (!ownerId.equals(store.getOwner().getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Subscription subscription = Subscription.create(
                store,
                request.getName(),
                request.getDescription(),
                request.getDay(),
                request.getMealTime(),
                request.getPrice(),
                request.getStock()
        );

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        return CreateSubscriptionResponse.from(savedSubscription);
    }

    // 전체 구독 목록 조회
    @Transactional(readOnly = true)
    public Slice<GetSubscriptionDistanceResponse> getSubscriptionSlice(SearchSubscriptionCond cond) {
        double lastDistance = cond.getLastDistance() != null ? cond.getLastDistance() : -1;
        long lastStoreId = cond.getLastStoreId() != null ? cond.getLastStoreId() : 0L;

        //최종 응답 개수
        int limitSize = cond.getSize() != null ? cond.getSize() : 10;

        //매장당 최대 3개
        int perStoreLimit = 3;

        //후보 id List 넉넉히 가져오기 (Math.ceil은 올림)
        int minStores = (int) Math.ceil((double) (limitSize + 1) / perStoreLimit);
        int storeSize = Math.max(50, minStores * 20);

        //1. 매장 근처 찾기 후보 id List
        List<StoreRedisDto> nearStores = storeRedisService.get(
                cond.getNowLongitude(),
                cond.getNowLatitude(),
                lastDistance,
                lastStoreId,
                storeSize + 1
        );

        //근처 매장 없을 때
        if (nearStores == null || nearStores.isEmpty()) {
            return new SliceImpl<>(List.of(), PageRequest.of(0, limitSize), false);
        }

        //DB에 보낼 id List 최소화 하기
        boolean storeHasMore = nearStores.size() > storeSize;
        if (storeHasMore) {
            nearStores = nearStores.subList(0, storeSize);
        }

        Map<Long, Double> distanceMap = nearStores.stream()
                .collect(Collectors.toMap(StoreRedisDto::getId, StoreRedisDto::getDistance, (a, b) -> a));

        List<Long> storeIds = nearStores.stream()
                .map(StoreRedisDto::getId)
                .toList();

        //2. 매장 당 perStoreLimit만큼 조회
        List<SubscriptionDto> dtoList = subscriptionRepository.findByCond(
                storeIds,
                cond.getCategory() == null ? null : cond.getCategory().name(),
                cond.getKeyword(),
                perStoreLimit
        );

        Map<Long, List<SubscriptionDto>> grouped = dtoList.stream()
                .collect(Collectors.groupingBy(SubscriptionDto::storeId));

        List<GetSubscriptionDistanceResponse> result = new ArrayList<>();

        //목표 사이즈
        int target = limitSize + 1;

        for (Long storeId : storeIds) {
            List<SubscriptionDto> list = grouped.getOrDefault(storeId, List.of());
            Double distance = distanceMap.get(storeId);
            for (SubscriptionDto dto : list) {
                if (result.size() >= target) break;
                result.add(GetSubscriptionDistanceResponse.from(dto, distance));
            }
            if (result.size() >= target) break;
        }

        boolean hasNext = result.size() > limitSize;
        if (hasNext) {
            result = result.subList(0, limitSize);
        }

        return new SliceImpl<>(result, PageRequest.of(0, limitSize), hasNext);
    }


    // 특정 매장 구독 목록 조회
    @Transactional(readOnly = true)
    public List<GetSubscriptionResponse> getSubscriptionListByStore(Long storeId) {
        List<Subscription> subscriptionList = subscriptionRepository.findByStoreIdAndIsDeletedIsFalse(storeId);

        return subscriptionList.stream().map(GetSubscriptionResponse::from).toList();
    }

    // 구독 상세 조회
    @Transactional(readOnly = true)
    public GetSubscriptionResponse getSubscription(Long subscriptionId) {
        Subscription subscription = getSubscriptionOrThrow(subscriptionId);

        return GetSubscriptionResponse.from(subscription);
    }

    // 구독 수정
    @Transactional
    public UpdateSubscriptionResponse updateSubscription(UpdateSubscriptionRequest request, Long subscriptionId) {
        Subscription subscription = getSubscriptionOrThrow(subscriptionId);

        subscription.changeIsJoinable(request.getJoinable());

        subscriptionRepository.flush();

        return UpdateSubscriptionResponse.from(subscription);
    }

    // 구독 삭제
    @Transactional
    public void deleteSubscription(AuthUser authUser, Long subscriptionId) {
        Subscription subscription = getSubscriptionOrThrow(subscriptionId);

        checkPermission(authUser, subscription);

        subscription.delete();
    }

    //권한 확인
    @Transactional
    public void checkPermission(AuthUser authUser, Subscription subscription) {
        Long ownerId = subscription.getStore().getOwner().getId();

        if (UserRole.ADMIN == authUser.getUserRole()) {
            return;
        }

        if (UserRole.OWNER == authUser.getUserRole() && ownerId.equals(authUser.getUserId())) {
            return;
        }

        throw new CustomException(ErrorCode.FORBIDDEN);
    }

    //구독 조회
    @Transactional
    public Subscription getSubscriptionOrThrow(Long id) {
        return subscriptionRepository.findByIdAndIsDeletedIsFalse(id).orElseThrow(
                () -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
    }

    //구독 재고 차감
    @Transactional
    public void decreaseById(Long id) {
        int change = subscriptionRepository.decreaseStock(id);

        if (change == 0) {
            throw new CustomException(ErrorCode.SUBSCRIPTION_OUT_OF_STOCK);
        }
    }

    //구독 재고 증가
    @Transactional
    public void increaseById(Long id, Integer count) {
        int change = subscriptionRepository.increaseStock(id);
    }
}

