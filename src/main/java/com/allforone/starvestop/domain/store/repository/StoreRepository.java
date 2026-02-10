package com.allforone.starvestop.domain.store.repository;

import com.allforone.starvestop.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByIdAndIsDeletedIsFalse(Long id);

    Page<Store> findByOwnerIdAndIsDeletedIsFalseOrderByName(Long ownerId, Pageable pageable);
    @Query(value = """
    select distinct s.*
    from stores s left join products p on s.id = p.store_id and p.is_deleted = 0
    where s.id in (:ids)
        and s.is_deleted = 0
        and (match(s.name) against(:keyword in natural language mode) or match(p.name) against(:keyword in natural language mode))
        and (:category is null or s.category = :category)
    """, nativeQuery = true)
    List<Store> findByIdsWithFullText(@Param("ids")List<Long> ids, @Param("keyword")String keyword, @Param("category")String category);

    @Query(value = """
    select distinct s.*
    from stores s
    where s.id in (:ids)
        and s.is_deleted = 0
        and (:category is null or s.category = :category)
    """, nativeQuery = true)
    List<Store> findByIds(@Param("ids")List<Long> ids, @Param("category")String category);
}
