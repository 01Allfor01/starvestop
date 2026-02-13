package com.allforone.starvestop.domain.cart.repository;

import com.allforone.starvestop.domain.cart.dto.CartListResponse;
import com.allforone.starvestop.domain.cart.dto.CartResponse;
import com.allforone.starvestop.domain.cart.entity.QCart;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.allforone.starvestop.domain.cart.entity.QCart.cart;
import static com.allforone.starvestop.domain.product.entity.QProduct.product;
import static com.allforone.starvestop.domain.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public CartListResponse findCartListAndStoreIdByUserId(Long userId) {
        QCart cart2 = new QCart("cart2");
        QCart cart3 = new QCart("cart3");

        Long lastVisitStoreId = queryFactory
                .select(cart.product.store.id)
                .from(cart)
                .where(cart.user.id.eq(userId))
                .orderBy(cart.createdAt.desc())
                .fetchFirst();

        List<CartResponse> cartList = queryFactory
                .select(Projections.constructor(CartResponse.class,
                        cart.id,
                        product.id,
                        product.name,
                        cart.quantity))
                .from(cart)
                .join(cart.product, product)
                .join(product.store, store)
                .where(
                        cart.user.id.eq(userId),
                        store.id.eq(lastVisitStoreId)
                ).fetch();

        return new CartListResponse(lastVisitStoreId, cartList);
    }
}
