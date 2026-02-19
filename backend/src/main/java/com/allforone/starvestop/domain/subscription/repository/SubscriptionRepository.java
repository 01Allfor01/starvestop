package com.allforone.starvestop.domain.subscription.repository;

import com.allforone.starvestop.domain.subscription.dto.SubscriptionDto;
import com.allforone.starvestop.domain.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByIsDeletedIsFalse();

    List<Subscription> findByStoreIdAndIsDeletedIsFalse(Long storeId);

    Optional<Subscription> findByIdAndIsDeletedIsFalse(Long id);

    @Modifying(clearAutomatically = true)
    @Query("update Subscription s set s.stock = s.stock - 1 where s.id = :id and s.stock > 0")
    int decreaseStock(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("update Subscription s set s.stock = s.stock + 1 where s.id = :id")
    int increaseStock(@Param("id") Long id);

    @Query(value = """
            select
                t.id          as id,
                t.store_id    as storeId,
                t.store_name  as storeName,
                t.name        as name,
                t.description as description,
                t.day         as day,
                t.meal_time   as mealTime,
                t.price       as price,
                t.stock       as stock,
                t.is_joinable as isJoinable
            from (
                select
                    s.id              as id,
                    st.id             as store_id,
                    st.name           as store_name,
                    s.name            as name,
                    s.description     as description,
                    s.day             as day,
                    s.meal_time       as meal_time,
                    s.price           as price,
                    s.stock           as stock,
                    s.is_joinable     as is_joinable,
                    row_number() over (partition by st.id order by s.id asc) as rn
                from subscriptions s
                join stores st on st.id = s.store_id
                where st.id in (:storeIds)
                  and st.is_deleted = false
                  and s.is_deleted = false
                  and (:category is null or st.category = :category)
                  and (:keyword is null or s.name like concat('%', :keyword, '%'))
            ) t
            where t.rn <= :perStoreLimit
            order by t.store_id asc, t.id asc
            """, nativeQuery = true)
    List<SubscriptionDto> findByCond(
            @Param("storeIds") List<Long> storeIds,
            @Param("category") String category,
            @Param("keyword") String keyword,
            @Param("perStoreLimit") int perStoreLimit
    );


}
