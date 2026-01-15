package com.allforone.starvestop.domain.product.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.product.dto.request.CreateProductRequest;
import com.allforone.starvestop.domain.product.dto.response.CreateProductResponse;
import com.allforone.starvestop.domain.product.dto.response.GetProductSaleResponse;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import com.allforone.starvestop.domain.product.repository.ProductRepository;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.repository.StoreRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    //상품 추가
    @Transactional
    public CreateProductResponse createProduct(@Valid CreateProductRequest request) {
        Store store = storeRepository.findById(request.getStoreId()).orElseThrow(
                () -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        Product product = Product.create(store,
                request.getProductName(),
                request.getDescription(),
                request.getPrice(),
                request.getSalePrice(),
                ProductStatus.valueOf(request.getStatus()));

        Product savedProduct = productRepository.save(product);

        return CreateProductResponse.from(savedProduct);
    }

    //마감 세일 상품 목록 조회
    @Transactional(readOnly = true)
    public List<GetProductSaleResponse> getProductSaleList() {

        List<Product> productList = productRepository.findAllByStatus(ProductStatus.SALE);

        return productList
                .stream()
                .map(GetProductSaleResponse::from)
                .toList();
    }
}
