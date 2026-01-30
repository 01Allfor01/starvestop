package com.allforone.starvestop.domain.apilog.repository;

import com.allforone.starvestop.domain.apilog.dto.ApiLogSearchCond;
import com.allforone.starvestop.domain.apilog.entity.ApiLog;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.allforone.starvestop.domain.apilog.entity.QApiLog.apiLog;

@Repository
@RequiredArgsConstructor
public class ApiLogRepositoryImpl implements ApiLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ApiLog> search(ApiLogSearchCond cond, Pageable pageable) {
        List<ApiLog> content = queryFactory
                .selectFrom(apiLog)
                .where(
                        userIdEq(cond.getUserId()),
                        userNameEq(cond.getUserName()),
                        userRoleEq(cond.getUserRole()),
                        httpMethodEq(cond.getHttpMethod()),
                        isSuccessEq(cond.getIsSuccess())
                )
                .orderBy(apiLog.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(apiLog.count())
                .from(apiLog)
                .where(
                        userIdEq(cond.getUserId()),
                        userNameEq(cond.getUserName()),
                        userRoleEq(cond.getUserRole()),
                        httpMethodEq(cond.getHttpMethod()),
                        isSuccessEq(cond.getIsSuccess())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression userIdEq(String userId) {
        return StringUtils.hasText(userId) ? apiLog.userId.eq(userId) : null;
    }

    private BooleanExpression userNameEq(String userName) {
        return StringUtils.hasText(userName) ? apiLog.userName.eq(userName) : null;
    }

    private BooleanExpression userRoleEq(String userRole) {
        return StringUtils.hasText(userRole) ? apiLog.userRole.eq(userRole) : null;
    }

    private BooleanExpression httpMethodEq(String httpMethod) {
        return StringUtils.hasText(httpMethod) ? apiLog.httpMethod.eq(httpMethod) : null;
    }

    private BooleanExpression isSuccessEq(Boolean isSuccess) {
        return isSuccess != null ? apiLog.isSuccess.eq(isSuccess) : null;
    }
}