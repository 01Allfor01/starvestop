package com.allforone.starvestop.domain.product.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductFunction {

    private final ProductRepository productRepository;

    public Product getById(Long id) {
        return productRepository.findByIdAndIsDeletedIsFalse(id).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)
        );
    }

    public void decreaseById(Long id, Integer count) {
        Product product = getById(id);
        product.decrease(count);
    }

    public void increaseById(Long id, Integer count) {
        Product product = getById(id);
        product.increase(count);
    }
}
