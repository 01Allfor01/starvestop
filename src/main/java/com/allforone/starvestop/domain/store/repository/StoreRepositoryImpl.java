package com.allforone.starvestop.domain.store.repository;

import com.allforone.starvestop.common.utils.GeometryUtil;
import com.allforone.starvestop.domain.product.entity.QProduct;
import com.allforone.starvestop.domain.store.dto.condition.SearchStoreCond;
import com.allforone.starvestop.domain.store.dto.response.StoreListResponse;
import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.allforone.starvestop.domain.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StoreListResponse> searchStoreList(SearchStoreCond cond) {
        QProduct product = QProduct.product;

        return queryFactory
                .select(Projections.constructor(StoreListResponse.class,
                        store.id,
                        store.storeName,
                        store.address,
                        store.category,
                        store.location,
                        store.openTime,
                        store.closeTime,
                        store.status
                ))
                .from(store)
                .leftJoin(product).on(product.store.eq(store).and(product.isDeleted.isFalse()))
                .where(
                        store.isDeleted.isFalse(),
                        containsKeyword(cond.getKeyword(), product),
                        eqCategory(cond.getCategory())
                )
                .groupBy(store.id)
                .orderBy(createDistanceSort(cond.getNowLatitude(), cond.getNowLongitude()))
                .fetch();
    }

    private BooleanExpression containsKeyword(String keyword, QProduct product) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        return store.storeName.contains(keyword).or(product.productName.contains(keyword));
    }

    private BooleanExpression eqCategory(String category) {
        if (!StringUtils.hasText(category)) {
            return null;
        }
        return store.category.eq(StoreCategory.valueOf(category));
    }

    private OrderSpecifier<?> createDistanceSort(Double nowLatitude, Double nowLongitude) {
        if (nowLatitude == null || nowLongitude == null) {
            return store.id.desc();
        }

        Point now = GeometryUtil.createPoint(nowLongitude, nowLatitude);

        NumberExpression<Double> distanceExpression = Expressions.numberTemplate(Double.class,
                "ST_Distance_Sphere({0}, {1})",
                store.location,
                now
        );

        return distanceExpression.asc();
    }
}