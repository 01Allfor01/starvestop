package com.allforone.starvestop.domain.product.repository;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.allforone.starvestop.domain.product.entity.QProduct.product;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public long findByIdAndDecreaseStock(Long productId, Integer count) {
        long updated = queryFactory
                .update(product)
                .set(product.stock, product.stock.subtract(count))
                .where(
                        product.id.eq(productId),
                        product.isDeleted.isFalse(),
                        product.stock.goe(count)
                ).execute();

        if (updated == 0) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_ENOUGH_STOCK);
        }

        return updated;
    }

    @Override
    public long findByIdAndIncreaseStock(Long productId, Integer count) {
        return queryFactory
                .update(product)
                .set(product.stock, product.stock.add(count))
                .where(
                        product.id.eq(productId),
                        product.isDeleted.isFalse()
                ).execute();
    }
}
