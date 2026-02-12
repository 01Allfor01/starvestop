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

import java.util.ArrayList;
import java.util.List;

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

        //매장당 최대 몇개
        int perStoreLimit = 5;

        int target = limitSize + 1;

        List<GetProductSaleResponse> result = new ArrayList<>(target);

        //1. lastStoreId 있을 때
        if (lastStoreId != 0L) {
            int remain = target - result.size();
            int pageSize = Math.min(perStoreLimit, remain) + 1;

            Pageable pageable = PageRequest.of(0, pageSize);

            List<ProductSaleDto> first = productRepository.findByCond(
                    lastStoreId,
                    cond.getLastId(),
                    cond.getCategory(),
                    cond.getKeyword(),
                    pageable);

            int take = Math.min(first.size(), Math.min(perStoreLimit, remain));

            for (int i = 0; i < take; i++) {
                ProductSaleDto p = first.get(i);

                String imageUrl = s3Service.createPresignedGetUrl(
                        p.getId(),
                        S3BucketStatus.PRODUCT,
                        p.getImageUuid()
                );
                result.add(GetProductSaleResponse.from(p, null, imageUrl))
            }
        }




        //1. Redis에서 5km이내 매장 거리순으로 매장 id와 거리 가져오기
        List<StoreRedisDto> storeRedisList = storeRedisService.get(
                cond.getNowLongitude(),
                cond.getNowLatitude(),
                distance,
                lastStoreId,
                limitSize);

        //2. 매장이 없으면 빈 List 반환
        if (storeRedisList == null || storeRedisList.isEmpty()) {
            Pageable pageable = PageRequest.of(
                    (cond.getSize() == null ? 0 : 1),
                    limitSize
            );
            return new SliceImpl<>(List.of(), pageable, false);
        }

        List<GetProductSaleResponse> responseList = new ArrayList<>(limitSize + 1);

        boolean hasNext = false;

        //3. 매장을 거리순으로 순회하며 상품을 가져와서 채움
        for (StoreRedisDto storeRedisDto : storeRedisList) {

            if(responseList.size() >= limitSize + 1) {
                hasNext = true;
                break;
            }

            Long storeId = storeRedisDto.getId();

            //4. 마지막 매장 아이디면 lastId 적용 나머지는 커서 없음으로 null 처리
            Long cursorStore = (storeId == lastStoreId) ? cond.getLastId() : null;

            int remaining = (limitSize + 1) - responseList.size();
            int pageSize = Math.min(perStoreLimit, remaining) + 1;

            Pageable pageable = PageRequest.of(0, pageSize);

            List<ProductSaleDto> products = productRepository.findByCond(
                    storeId,
                    cursorStore,
                    cond.getCategory(),
                    cond.getKeyword(),
                    pageable
            );

            if (products.isEmpty()) continue;

            //해당 매장에 더 있니?
            boolean storeMore = products.size() > Math.min(perStoreLimit, remaining);

            //실제로 담을 개수만큼 자르기
            int take = Math.min(products.size(), Math.min(perStoreLimit, remaining));

            for (int i = 0; i < take; i++) {
                ProductSaleDto product = products.get(i);

                //이미지 발급
                String imageUrl = s3Service.createPresignedGetUrl(
                        product.getId(),
                        S3BucketStatus.PRODUCT,
                        product.getImageUuid()
                );

                responseList.add(GetProductSaleResponse.from(product, storeRedisDto, imageUrl));

                if (responseList.size() >= limitSize + 1) break;
            }

            //5. responseList가 매장에 상품이 더 남아있으면 다음페이지 존재
            if (storeMore) {
                hasNext = true;
            }

            //6. responseList가 다 차 있으면 break
            if (responseList.size() >= limitSize + 1) {
                hasNext = true;
                break;
            }
        }

        if (responseList.size() > limitSize) {
            hasNext = true;
            responseList.remove(limitSize);
        }

        Pageable resultPageable = PageRequest.of(0, limitSize);
        return new SliceImpl<>(responseList, resultPageable, hasNext);
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
