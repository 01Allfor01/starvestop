package com.allforone.starvestop.domain.store.service;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.enums.UserRole;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.common.utils.GeometryUtil;
import com.allforone.starvestop.domain.owner.entity.Owner;
import com.allforone.starvestop.domain.owner.service.OwnerService;
import com.allforone.starvestop.domain.s3.enums.S3BucketStatus;
import com.allforone.starvestop.domain.s3.service.S3Service;
import com.allforone.starvestop.domain.store.dto.request.CreateStoreRequest;
import com.allforone.starvestop.domain.store.dto.request.UpdateStoreRequest;
import com.allforone.starvestop.domain.store.dto.response.CreateStoreResponse;
import com.allforone.starvestop.domain.store.dto.response.GetStoreDetailResponse;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import com.allforone.starvestop.domain.store.event.StoreGeoUpsertedEvent;
import com.allforone.starvestop.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private S3Service s3Service;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private OwnerService ownerService;

    @InjectMocks
    private StoreService storeService;

    private AuthUser authUser;
    private AuthUser otherAuthUser;
    private Owner owner;
    private Store store;

    @BeforeEach
    void setUp() {
        authUser = AuthUser.of(1L, "lee@seo.jun", "leeseojun", UserRole.OWNER);
        otherAuthUser = AuthUser.of(2L, "lee@jun.beom", "leejunbeom", UserRole.OWNER);

        owner = Owner.create("lee@seo.jun", "password", "leeseojun");
        ReflectionTestUtils.setField(owner, "id", 1L);

        Owner otherOwner = Owner.create("lee@jun.beom", "password", "leejunbeom");
        ReflectionTestUtils.setField(otherOwner, "id", 2L);

        Point point = GeometryUtil.createPoint(127.001695, 37.5642135);

        store = Store.create(
                owner,
                "수원 왕 갈비 치킨",
                "지금은 새벽 다섯 시 반",
                "이곳이 갈비인가 치킨인가 수원 왕 갈비 통닭",
                StoreCategory.KOREAN_FOOD,
                point,
                LocalTime.of(9, 0),
                LocalTime.of(21, 0),
                StoreStatus.OPENED,
                "사업자 등록 번호"
        );
        ReflectionTestUtils.setField(store, "id", 10L);
        ReflectionTestUtils.setField(store, "imageUuid", "store-img-uuid");
    }

    @Test
    @DisplayName("매장_생성_성공")
    void createStore_success() {
        //given
        CreateStoreRequest request = new CreateStoreRequest();
        ReflectionTestUtils.setField(request, "name", "이 매장은 이제 제껍니다");
        ReflectionTestUtils.setField(request, "address", "서울 어딘가");
        ReflectionTestUtils.setField(request, "description", "제가 마음대로 할 수 있습니다.");
        ReflectionTestUtils.setField(request, "category", StoreCategory.KOREAN_FOOD);
        ReflectionTestUtils.setField(request, "longitude", 127.1234);
        ReflectionTestUtils.setField(request, "latitude", 37.5678);
        ReflectionTestUtils.setField(request, "openTime", LocalTime.of(8, 0));
        ReflectionTestUtils.setField(request, "closeTime", LocalTime.of(20, 0));
        ReflectionTestUtils.setField(request, "status", StoreStatus.OPENED);
        ReflectionTestUtils.setField(request, "businessRegistrationNumber", "123-45-67890");

        Store saved = Store.create(
                owner,
                request.getName(),
                request.getAddress(),
                request.getDescription(),
                request.getCategory(),
                GeometryUtil.createPoint(request.getLongitude(), request.getLatitude()),
                request.getOpenTime(),
                request.getCloseTime(),
                StoreStatus.CLOSED,
                request.getBusinessRegistrationNumber()
        );
        ReflectionTestUtils.setField(saved, "id", 99L);

        when(ownerService.getById(1L)).thenReturn(owner);
        when(storeRepository.save(any(Store.class))).thenReturn(saved);

        //when
        CreateStoreResponse response = storeService.createStore(1L, request);

        //then
        assertNotNull(response);
        assertEquals(99L, response.getId());

        verify(ownerService).getById(1L);
        verify(storeRepository).save(any(Store.class));
    }

    @Test
    @DisplayName("매장_수정_성공_올바른_owner")
    void updateStore_success_owner() {
        //given
        UpdateStoreRequest request = new UpdateStoreRequest();
        ReflectionTestUtils.setField(request, "name", "수정된 매장");
        ReflectionTestUtils.setField(request, "address", "수정된 주소");
        ReflectionTestUtils.setField(request, "description", "수정된 설명");
        ReflectionTestUtils.setField(request, "category", StoreCategory.KOREAN_FOOD);
        ReflectionTestUtils.setField(request, "longitude", 128.0001);
        ReflectionTestUtils.setField(request, "latitude", 38.0002);
        ReflectionTestUtils.setField(request, "openTime", LocalTime.of(10, 0));
        ReflectionTestUtils.setField(request, "closeTime", LocalTime.of(22, 0));
        ReflectionTestUtils.setField(request, "status", StoreStatus.OPENED);

        when(storeRepository.findByIdAndIsDeletedIsFalse(10L)).thenReturn(Optional.of(store));

        //when
        CreateStoreResponse response = storeService.updateStore(authUser, 10L, request);

        //then
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("수정된 매장", response.getName());

        verify(storeRepository).findByIdAndIsDeletedIsFalse(10L);
        verify(storeRepository).flush();
    }

    @Test
    @DisplayName("매장_수정_실패_FORBIDDEN_다른_owner")
    void updateStore_fail_forbidden_otherOwner() {
        //given
        UpdateStoreRequest request = new UpdateStoreRequest();
        ReflectionTestUtils.setField(request, "name", "내가 수정을 해볼게 얍");

        when(storeRepository.findByIdAndIsDeletedIsFalse(10L)).thenReturn(Optional.of(store));

        //when
        CustomException ex = assertThrows(CustomException.class,
                () -> storeService.updateStore(otherAuthUser, 10L, request));

        //then
        assertEquals(ErrorCode.FORBIDDEN, ex.getErrorCode());

        verify(storeRepository).findByIdAndIsDeletedIsFalse(10L);
        verify(storeRepository, never()).flush();

        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("매장_삭제_성공_올바른_owner")
    void deleteStore_success_owner() {
        //given
        when(storeRepository.findByIdAndIsDeletedIsFalse(10L)).thenReturn(Optional.of(store));

        //when
        storeService.deleteStore(authUser, 10L);

        //then
        verify(storeRepository).findByIdAndIsDeletedIsFalse(10L);

        Object isDeleted = ReflectionTestUtils.getField(store, "isDeleted");
        if (isDeleted != null) {
            assertTrue((Boolean) isDeleted);
        }
    }

    @Test
    @DisplayName("매장_삭제_실패_FORBIDDEN_다른_owner")
    void deleteStore_fail_forbidden_otherOwner() {
        //given
        when(storeRepository.findByIdAndIsDeletedIsFalse(10L)).thenReturn(Optional.of(store));

        //when
        CustomException ex = assertThrows(CustomException.class,
                () -> storeService.deleteStore(otherAuthUser, 10L));

        //then
        assertEquals(ErrorCode.FORBIDDEN, ex.getErrorCode());

        verify(storeRepository).findByIdAndIsDeletedIsFalse(10L);
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("매장_상세_조회_성공")
    void getStoreDetail_success() {
        //ㅎiven
        when(storeRepository.findByIdAndIsDeletedIsFalse(10L)).thenReturn(Optional.of(store));
        when(s3Service.createPresignedGetUrl(10L, S3BucketStatus.STORE, "store-img-uuid"))
                .thenReturn("https://presigned-url/store/10");

        //when
        GetStoreDetailResponse response = storeService.getStoreDetail(10L);

        //then
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("수원 왕 갈비 치킨", response.getName());
        assertEquals("https://presigned-url/store/10", response.getImageUrl());

        verify(storeRepository).findByIdAndIsDeletedIsFalse(10L);
        verify(s3Service).createPresignedGetUrl(10L, S3BucketStatus.STORE, "store-img-uuid");
    }

    @Test
    @DisplayName("매장_상세_조회_실패_매장없음")
    void getStoreDetail_fail_notFound() {
        //given
        when(storeRepository.findByIdAndIsDeletedIsFalse(999L)).thenReturn(Optional.empty());

        //when
        CustomException ex = assertThrows(CustomException.class,
                () -> storeService.getStoreDetail(999L));

        //then
        assertEquals(ErrorCode.STORE_NOT_FOUND, ex.getErrorCode());

        verify(storeRepository).findByIdAndIsDeletedIsFalse(999L);
        verifyNoInteractions(s3Service);
    }
}