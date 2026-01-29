package com.allforone.starvestop.domain.store.repository;

import com.allforone.starvestop.common.utils.GeometryUtil;
import com.allforone.starvestop.domain.store.dto.condition.SearchStoreCond;
import com.allforone.starvestop.domain.store.dto.response.StoreDto;
import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.allforone.starvestop.domain.product.entity.QProduct.product;
import static com.allforone.starvestop.domain.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<StoreDto> searchStorePage(SearchStoreCond cond) {

        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());

        List<StoreDto> list = queryFactory
                .select(Projections.constructor(StoreDto.class,
                        store.id,
                        store.storeName,
                        store.address,
                        store.category,
                        store.location,
                        store.openTime,
                        store.closeTime,
                        store.status,
                        store.imageUuid
                ))
                .from(store)
                .leftJoin(product).on(product.store.eq(store).and(product.isDeleted.isFalse()))
                .where(
                        store.isDeleted.isFalse(),
                        containsKeyword(cond.getKeyword()),
                        eqCategory(cond.getCategory())
                )
                .groupBy(store.id)
                .orderBy(createDistanceSort(cond.getNowLatitude(), cond.getNowLongitude()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(store.countDistinct())
                .from(store)
                .where(
                        store.isDeleted.isFalse(),
                        containsKeyword(cond.getKeyword()),
                        eqCategory(cond.getCategory())
                )
                .groupBy(store.id)
                .fetchOne();

        long total = count != null ? count : 0L;

        return new PageImpl<>(list, pageable, total);
    }

    private BooleanExpression containsKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        return store.storeName.contains(keyword).or(product.name.contains(keyword));
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