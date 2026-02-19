package com.allforone.starvestop.domain.product.repository;

public interface ProductRepositoryCustom {
    long findByIdAndDecreaseStock(Long productId, Integer count);

    long findByIdAndIncreaseStock(Long productId, Integer count);
}
