package com.allforone.starvestop.domain.store.repository;

import com.allforone.starvestop.common.utils.GeometryUtil;
import com.allforone.starvestop.domain.store.dto.condition.SearchStoreCond;
import com.allforone.starvestop.domain.store.dto.response.StoreDto;
import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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
    public Slice<StoreDto> searchStoreSlice(SearchStoreCond cond) {

        NumberExpression<Double> distance = distanceExpression(cond.getNowLatitude(), cond.getNowLongitude());

        List<StoreDto> list = queryFactory
                .select(Projections.constructor(StoreDto.class,
                        store.id,
                        store.name,
                        store.address,
                        store.category,
                        store.location,
                        store.openTime,
                        store.closeTime,
                        store.status,
                        store.imageUuid
                ))
                .distinct()
                .from(store)
                .where(
                        store.isDeleted.isFalse(),
                        boundingBox(cond.getNowLatitude(), cond.getNowLongitude()),
                        withinDistance(distance),
                        eqCategory(cond.getCategory()),
                        containsKeywordStore(cond.getKeyword()),
                        cursorIdLt(cond.getCursorId())
                )
                .orderBy(distance.asc(), store.id.desc())
                .limit(cond.getSize() + 1)
                .fetch();

        boolean hasNext = false;

        if (list.size() > cond.getSize()) {
            list.remove(cond.getSize());
            hasNext = true;
        }

        return new SliceImpl<>(list, PageRequest.of(0, cond.getSize()), hasNext);
    }

    //매장 이름 keyword 포함 확인
    private BooleanExpression containsKeywordStore(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        return store.name.contains(keyword).or(containsKeywordProduct(keyword));
    }

    //상품 이름 keyword 포함 확인
    private BooleanExpression containsKeywordProduct(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        return JPAExpressions
                .selectOne()
                .from(product)
                .where(
                        product.store.eq(store),
                        product.isDeleted.isFalse(),
                        product.name.contains(keyword)
                )
                .exists();
    }

    //boundingBox
    private BooleanExpression boundingBox(Double nowLatitude, Double nowLongitude) {
        if (nowLatitude == null || nowLongitude == null) {
            return null;
        }

        double deltaLat = (double) 5 / 111.0;
        double deltaLon = (double) 5 / (111.0 * Math.cos(Math.toRadians(nowLatitude)));

        double minLat = nowLatitude - deltaLat;
        double maxLat = nowLatitude + deltaLat;
        double minLon = nowLongitude - deltaLon;
        double maxLon = nowLongitude + deltaLon;

        String wkt = String.format(
                "POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))",
                minLat, minLon,
                minLat, maxLon,
                maxLat, maxLon,
                maxLat, minLon,
                minLat, minLon
        );

        NumberExpression<Integer> contains = Expressions.numberTemplate(
                Integer.class,
                "MBRContains(ST_GeomFromText({0}, 4326), {1}) ",
                wkt,
                store.location
        );

        return contains.eq(1);
    }

    //거리 nkm 이내 조건 (5km이내)
    private BooleanExpression withinDistance(NumberExpression<Double> distance) {
        if (distance == null) {
            return null;
        }

        return distance.loe(5000.0);
    }

    //거리 계산
    private NumberExpression<Double> distanceExpression(Double nowLatitude, Double nowLongitude) {
        if (nowLatitude == null || nowLongitude == null) {
            return null;
        }

        Point now = GeometryUtil.createPoint(nowLongitude, nowLatitude);

        return Expressions.numberTemplate(Double.class,
                "ST_Distance_Sphere({0}, {1})",
                store.location,
                now
        );
    }

    //매장 카테고리 확인
    private BooleanExpression eqCategory(String category) {
        if (!StringUtils.hasText(category)) {
            return null;
        }
        return store.category.eq(StoreCategory.valueOf(category));
    }

    //커서 기반 id
    private BooleanExpression cursorIdLt(Long cursorId) {
        return (cursorId == null) ? null : store.id.lt(cursorId);
    }
}