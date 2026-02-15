package com.allforone.starvestop.domain.product.service;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.enums.UserRole;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.product.dto.ProductSaleDto;
import com.allforone.starvestop.domain.product.dto.condition.SearchProductCond;
import com.allforone.starvestop.domain.product.dto.request.CreateProductRequest;
import com.allforone.starvestop.domain.product.dto.request.UpdateProductRequest;
import com.allforone.starvestop.domain.product.dto.response.*;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.repository.ProductRepository;
import com.allforone.starvestop.domain.s3.enums.S3BucketStatus;
import com.allforone.starvestop.domain.s3.service.S3Service;
import com.allforone.starvestop.domain.store.dto.StoreRedisDto;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.service.StoreRedisService;
import com.allforone.starvestop.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final S3Service s3Service;
    private final ProductRepository productRepository;
    private final StoreService storeService;
    private final StoreRedisService storeRedisService;

    //특정 매장 상품 추가
    @Transactional
    public CreateProductResponse createProduct(AuthUser authUser, CreateProductRequest request) {
        Store store = storeService.getById(request.getStoreId());

        storeService.idMismatchCheck(authUser, store);

        Product product = Product.create(store,
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getSalePrice(),
                request.getStock(),
                request.getStatus());

        Product savedProduct = productRepository.save(product);

        return CreateProductResponse.from(savedProduct);
    }

    //특정 매장 상품 목록 조회
    @Transactional(readOnly = true)
    public Slice<GetProductResponse> getProductStoreSlice(Long storeId, Long lastId, Integer size) {
        Store store = storeService.getById(storeId);

        int limitSize = size != null ? size : 10;

        Slice<Product> productSlice = productRepository.findSliceByCursor(store.getId(), lastId, limitSize);

        return productSlice.map(product -> {

            String imageUrl = s3Service.createPresignedGetUrl(product.getId(), S3BucketStatus.PRODUCT, product.getImageUuid());

            return GetProductResponse.from(product, imageUrl);
        });
    }

    //마감 세일 상품 목록 조회
    @Transactional(readOnly = true)
    public Slice<GetProductSaleResponse> getProductSaleSlice(SearchProductCond cond) {
        double lastDistance = cond.getLastDistance() != null ? cond.getLastDistance() : -1;
        long lastStoreId = cond.getLastStoreId() != null ? cond.getLastStoreId() : 0L;
        int limitSize = cond.getSize() != null ? cond.getSize() : 10;

        //매장 당 n개의 마감 세일 상품
        int perStoreLimit = 3;

        int storeSize = Math.max(10, (int) Math.ceil((double) limitSize / perStoreLimit) * 5);

        //Redis에서 현재 좌표에서 후보 List 가져오기
        List<StoreRedisDto> nearStores = storeRedisService.get(
                cond.getNowLongitude(),
                cond.getNowLatitude(),
                lastDistance,
                lastStoreId,
                storeSize + 1
        );

        if (nearStores == null || nearStores.isEmpty()) {
            return new SliceImpl<>(List.of(), PageRequest.of(0, limitSize), false);
        }

        boolean hasNext = nearStores.size() > limitSize;
        if (hasNext) nearStores = nearStores.subList(0, limitSize);

        List<Long> storeIds = nearStores.stream().map(StoreRedisDto::getId).toList();

        //거리 매핑
        Map<Long, Double> distanceMap = nearStores.stream()
                .collect(Collectors.toMap(StoreRedisDto::getId, StoreRedisDto::getDistance));

        //DB 1번 쿼리로 “매장별 3개” 잘린 결과를 한 번에 가져옴
        List<ProductSaleDto> dtoList = productRepository.findSaleByCond(
                storeIds,
                cond.getCategory() == null ? null : cond.getCategory().name(),
                cond.getKeyword(),
                perStoreLimit
        );

        //response 만들기
        Map<Long, List<ProductSaleDto>> grouped = dtoList.stream()
                .collect(Collectors.groupingBy(ProductSaleDto::storeId));

        List<GetProductSaleResponse> result = new ArrayList<>();

        for (Long storeId : storeIds) {
            List<ProductSaleDto> list = grouped.getOrDefault(storeId, List.of());
            for (ProductSaleDto dto : list) {
                if (result.size() >= limitSize) break;

                String imageUrl = s3Service.createPresignedGetUrl(
                        dto.id(),
                        S3BucketStatus.PRODUCT,
                        dto.imageUuid()
                );

                result.add(GetProductSaleResponse.from(dto, distanceMap.get(storeId), imageUrl));
            }
            if (result.size() >= limitSize) break;
        }

        return new SliceImpl<>(result, PageRequest.of(0, limitSize), hasNext);
    }

    //상품 상세 조회
    @Transactional(readOnly = true)
    public GetProductDetailResponse getProductDetail(Long productId) {
        Product product = getProduct(productId);

        String imageUrl = s3Service.createPresignedGetUrl(product.getId(), S3BucketStatus.PRODUCT, product.getImageUuid());

        return GetProductDetailResponse.from(product, imageUrl);
    }

    //특정 매장 상품 수정
    @Transactional
    public UpdateProductResponse updateProduct(AuthUser authUser, Long productId, UpdateProductRequest request) {
        Product product = getProduct(productId);

        idMismatchCheck(authUser, product);

        product.update(
                request.getName(),
                request.getDescription(),
                request.getStock(),
                request.getPrice(),
                request.getSalePrice(),
                request.getStatus());

        productRepository.flush();

        return UpdateProductResponse.from(product);
    }

    //상품 삭제
    @Transactional
    public void delete(AuthUser authUser, Long productId) {
        Product product = getProduct(productId);

        idMismatchCheck(authUser, product);

        product.delete();
    }

    //상품 확인
    @Transactional
    public Product getProduct(Long productId) {
        return productRepository.findByIdAndIsDeletedIsFalse(productId).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    //판매자 아이디 주인 확인
    public void idMismatchCheck(AuthUser authUser, Product product) {
        if (UserRole.ADMIN == authUser.getUserRole()) {
            return;
        }

        Long ownerId = product.getStore().getOwner().getId();
        if (ownerId.equals(authUser.getUserId())) {
            return;
        }

        throw new CustomException(ErrorCode.FORBIDDEN);
    }

    //상품 재고 차감(-)
    @Transactional
    public void decreaseById(Long id, Integer count) {
        productRepository.findByIdAndDecreaseStock(id, count);
    }

    //상품 재고 가산(+)
    @Transactional
    public void increaseById(Long id, Integer count) {
        productRepository.findByIdAndIncreaseStock(id, count);
    }
}
