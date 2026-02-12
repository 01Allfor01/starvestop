package com.allforone.starvestop.domain.store.repository;

import com.allforone.starvestop.domain.store.dto.StoreDto;
import com.allforone.starvestop.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.allforone.starvestop.domain.store.enums.StoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {

    Optional<Store> findByIdAndIsDeletedIsFalse(Long id);

    Page<Store> findByOwnerIdAndIsDeletedIsFalseOrderByName(Long ownerId, Pageable pageable);

    @Query("""
    select distinct new com.allforone.starvestop.domain.store.dto.StoreDto(
        s.id,
        s.name,
        s.address,
        s.category,
        s.location,
        s.openTime,
        s.closeTime,
        s.status,
        s.imageUuid,
        s.updatedAt
    )
    from Store s left join Product p on p.store = s and p.isDeleted = false
    where s.id in (:ids)
        and s.isDeleted = false
        and (:category is null or s.category = :category)
        and (:keyword is null or s.name like concat('%', :keyword, '%') or p.name like concat('%', :keyword, '%'))
    """)
    List<StoreDto> findWithKeywordStoreDtoList(@Param("ids")List<Long> ids, @Param("keyword")String keyword, @Param("category")StoreCategory category);

    @Query("""
    select distinct new com.allforone.starvestop.domain.store.dto.StoreDto(
        s.id,
        s.name,
        s.address,
        s.category,
        s.location,
        s.openTime,
        s.closeTime,
        s.status,
        s.imageUuid,
        s.updatedAt
    )
    from Store s
    where s.id in :ids
        and s.isDeleted = false
        and (:category is null or s.category = :category)
    """)
    List<StoreDto> findStoreDtoList(@Param("ids")List<Long> ids, @Param("category")StoreCategory category);
}
