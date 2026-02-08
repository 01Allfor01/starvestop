package com.allforone.starvestop.domain.product.service;

import com.allforone.starvestop.common.dto.AuthUser;
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
import com.allforone.starvestop.domain.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        double distance = cond.getLastDistance() != null ? cond.getLastDistance() : -1;
        long lastStoreId = cond.getLastStoreId() != null ? cond.getLastStoreId() : 0L;
        int limitSize = cond.getSize() != null ? cond.getSize() : 10;

        //1. Redis에서 5km이내 매장 거리순으로 매장 id와 거리 가져오기
        List<StoreRedisDto> storeRedisList = storeRedisService.get(
                cond.getNowLongitude(),
                cond.getNowLatitude(),
                distance,
                lastStoreId,
                limitSize);

        //2. 매장이 없으면 빈 List 반환
        if (storeRedisList.isEmpty()) {
            Pageable pageable = PageRequest.of(
                    (cond.getSize() == null ? 0 : 1),
                    limitSize
            );
            return new SliceImpl<>(List.of(), pageable, false);
        }

        //3. 위의 List를 id만 가져오기
        List<Long> ids = storeRedisList
                .stream()
                .map(StoreRedisDto::getId)
                .toList();

        //4. id와 검색 필터를 가지고 DB 조회
        List<ProductSaleDto> list = productRepository
                .findByCond(ids, cond.getKeyword(), cond.getCategory(), cond.getLastId());

        //5. 매장 id 별 그룹화
        Map<Long, List<ProductSaleDto>> productMap = list
                .stream()
                .collect(Collectors.groupingBy(ProductSaleDto::getStoreId
                ));

        //6. 응답으로 변환
        List<GetProductSaleResponse> responseList = storeRedisList
                .stream()
                .filter(redisDto -> productMap.containsKey(redisDto.getId()))
                .flatMap(redisDto ->
                        productMap.get(redisDto.getId()).stream()
                                .map(product -> {

                                    String imageUrl = s3Service.createPresignedGetUrl(
                                            product.getId(),
                                            S3BucketStatus.PRODUCT,
                                            product.getImageUuid()
                                    );

                                    return GetProductSaleResponse.from(
                                            product,
                                            redisDto,
                                            imageUrl
                                    );
                                })
                )
                .limit(limitSize + 1)
                .collect(Collectors.toList());

        boolean hasNext = responseList.size() > limitSize;

        if (hasNext) {
            responseList.remove(limitSize);
        }

        Pageable pageable = PageRequest.of((cond.getSize() == null ? 0 : 1), limitSize);

        return new SliceImpl<>(responseList, pageable, hasNext);
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
        Product product = getProduct(id);
        product.decrease(count);
    }

    //상품 재고 가산(+)
    @Transactional
    public void increaseById(Long id, Integer count) {
        Product product = getProduct(id);
        product.increase(count);
    }
}
