//package com.allforone.starvestop.domain.product.service;
//
//import com.allforone.starvestop.common.dto.AuthUser;
//import com.allforone.starvestop.common.exception.CustomException;
//import com.allforone.starvestop.common.exception.ErrorCode;
//import com.allforone.starvestop.domain.product.dto.request.CreateProductRequest;
//import com.allforone.starvestop.domain.product.dto.request.UpdateProductRequest;
//import com.allforone.starvestop.domain.product.dto.response.*;
//import com.allforone.starvestop.domain.product.entity.Product;
//import com.allforone.starvestop.domain.product.enums.ProductStatus;
//import com.allforone.starvestop.domain.product.repository.ProductRepository;
//import com.allforone.starvestop.domain.store.entity.Store;
//import com.allforone.starvestop.domain.store.repository.StoreRepository;
//import com.allforone.starvestop.domain.user.enums.UserRole;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ProductService {
//
//    private final ProductRepository productRepository;
//    private final StoreRepository storeRepository;
//
//    //특정 매장 상품 추가
//    @Transactional
//    public CreateProductResponse createProduct(AuthUser authUser, CreateProductRequest request) {
//        Store store = getStoreOrThrow(request.getStoreId());
//
//        checkPermission(authUser, store.getOwner().getId());
//
//        Product product = Product.create(store,
//                request.getProductName(),
//                request.getDescription(),
//                request.getPrice(), request.getStock(),
//                request.getStatus());
//
//        Product savedProduct = productRepository.save(product);
//
//        return CreateProductResponse.from(savedProduct);
//    }
//
//    //매장 상품 목록 조회
//    @Transactional(readOnly = true)
//    public List<GetProductResponse> getProductStoreList(Long storeId) {
//        Store store = getStoreOrThrow(storeId);
//
//        List<Product> productSlice = productRepository.findAllByStoreAndIsDeletedIsFalse(store);
//
//        return productSlice
//                .stream()
//                .map(GetProductResponse::from)
//                .toList();
//    }
//
//    //마감 세일 상품 목록 조회
//    @Transactional(readOnly = true)
//    public List<GetProductSaleResponse> getProductSaleList() {
//        List<Product> productSlice = productRepository.findAllByStatusAndIsDeletedIsFalse(ProductStatus.SALE);
//
//        return productSlice
//                .stream()
//                .map(GetProductSaleResponse::from)
//                .toList();
//    }
//
//    //상품 상세 조회
//    @Transactional(readOnly = true)
//    public GetProductDetailResponse getProduct(Long productId) {
//        Product product = getProductOrThrow(productId);
//
//        return GetProductDetailResponse.from(product);
//    }
//
//    //특정 매장 상품 수정
//    @Transactional
//    public UpdateProductResponse updateProduct(AuthUser authUser, Long productId, UpdateProductRequest request) {
//        Product product = getProductOrThrow(productId);
//
//        checkPermission(authUser, product.getStore().getOwner().getId());
//
//        product.update(
//                request.getProductName(),
//                request.getDescription(),
//                request.getStock(),
//                request.getPrice(),
//                request.getStatus());
//
//        productRepository.flush();
//
//        return UpdateProductResponse.from(product);
//    }
//
//    //상품 삭제
//    @Transactional
//    public void delete(AuthUser authUser, Long productId) {
//        Product product = getProductOrThrow(productId);
//
//        checkPermission(authUser, product.getStore().getOwner().getId());
//
//        product.delete();
//    }
//
//    //권한 확인
//    public void checkPermission(AuthUser authUser, Long ownerId) {
//        if (UserRole.ADMIN == authUser.getUserRole()) {
//            return;
//        }
//
//        if (UserRole.OWNER == authUser.getUserRole() && ownerId.equals(authUser.getUserId())) {
//            return;
//        }
//
//        throw new CustomException(ErrorCode.FORBIDDEN);
//    }
//
//    //상품 조회
//    private Product getProductOrThrow(Long productId) {
//        return productRepository.findByIdAndIsDeletedIsFalse(productId).orElseThrow(
//                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
//    }
//
//    //매장 조회
//    private Store getStoreOrThrow(Long storeId) {
//        return storeRepository.findByIdAndIsDeletedIsFalse(storeId).orElseThrow(
//                () -> new CustomException(ErrorCode.STORE_NOT_FOUND));
//    }
//}
