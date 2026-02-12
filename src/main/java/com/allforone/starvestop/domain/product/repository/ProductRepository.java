package com.allforone.starvestop.domain.product.repository;

import com.allforone.starvestop.domain.product.dto.ProductSaleDto;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    Slice<Product> findAllByStatusAndIsDeletedIsFalse(ProductStatus status);

    Optional<Product> findByIdAndIsDeletedIsFalse(Long id);

    @Query("""
        select p
        from Product p
        where p.store.id = :storeId
            and p.isDeleted = false
            and (:lastId is null or p.id > :lastId)
        order by p.id asc
        limit :size
    """)
    Slice<Product> findSliceByCursor(@Param("storeId")Long storeId,
                                     @Param("lastId")Long lastId,
                                     @Param("size")int size);


    @Query(value = """
    select 
        id,
        store_id,
        store_name,
        name,
        description,
        stock,
        price,
        sale_price,
        image_uuid,
        updated_at
    from (
        select
            p.id            as id,
            s.id            as store_id,
            s.name          as store_name,
            p.name          as name,
            p.description   as description,
            p.stock         as stock,
            p.price         as price,
            p.sale_price    as sale_price,
            p.image_uuid    as image_uuid,
            p.updated_at    as updated_at,
            row_number() over (partition by s.id order by p.id asc) as rn
        from products p
        join stores s on s.id = p.store_id
        where s.id in (:storeIds)
          and s.is_deleted = false
          and p.is_deleted = false
          and p.status = 'SALE'
          and (:category is null or s.category = :category)
          and (:keyword is null or p.name like concat('%', :keyword, '%'))
    ) t
    where t.rn <= :perStoreLimit
    order by t.store_id asc, t.id asc
    """, nativeQuery = true)
    List<ProductSaleDto> findSaleByCond(
            @Param("storeIds") List<Long> storeIds,
            @Param("category") String category,
            @Param("keyword") String keyword,
            @Param("perStoreLimit") int perStoreLimit
    );
}
