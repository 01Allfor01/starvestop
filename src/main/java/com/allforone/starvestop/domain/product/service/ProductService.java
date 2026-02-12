package com.allforone.starvestop.domain.product.service;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.product.dto.request.CreateProductRequest;
import com.allforone.starvestop.domain.product.dto.request.UpdateProductRequest;
import com.allforone.starvestop.domain.product.dto.response.*;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import com.allforone.starvestop.domain.product.repository.ProductRepository;
import com.allforone.starvestop.domain.s3.enums.S3BucketStatus;
import com.allforone.starvestop.domain.s3.service.S3Service;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.service.StoreService;
import com.allforone.starvestop.domain.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final S3Service s3Service;
    private final ProductRepository productRepository;
    private final StoreService storeService;

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

    //매장 상품 목록 조회
    @Transactional(readOnly = true)
    public Slice<GetProductResponse> getProductStoreSlice(Long storeId, Pageable pageable) {
        Store store = storeService.getById(storeId);

        Slice<Product> productSlice = productRepository.findAllByStoreIdAndIsDeletedIsFalseOrderById(store.getId());

        return productSlice.map(product -> {

            String imageUrl = s3Service.createPresignedGetUrl(product.getId(), S3BucketStatus.PRODUCT, product.getImageUuid());

            return GetProductResponse.from(product, imageUrl);
        });
    }

    //마감 세일 상품 목록 조회
    @Transactional(readOnly = true)
    public Slice<GetProductSaleResponse> getProductSaleSlice(Pageable pageable) {
        Slice<Product> productSlice = productRepository.findAllByStatusAndIsDeletedIsFalse(ProductStatus.SALE);

        return productSlice.map(product -> {

            String imageUrl = s3Service.createPresignedGetUrl(product.getId(), S3BucketStatus.PRODUCT, product.getImageUuid());

            return GetProductSaleResponse.from(product, imageUrl);
        });
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
