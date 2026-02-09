package com.allforone.starvestop.domain.product.repository;

import com.allforone.starvestop.domain.product.dto.ProductSaleDto;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.store.enums.StoreCategory;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
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
    Slice<Product> findSliceByCursor(@Param("storeId")Long storeId, @Param("lastId")Long lastId, @Param("size")int size);

    @Query("""
    select new com.allforone.starvestop.domain.product.dto.ProductSaleDto(
        p.id,
        s.id,
        s.name,
        p.name,
        p.description,
        p.stock,
        p.price,
        p.salePrice,
        p.imageUuid,
        p.updatedAt
    )
    from Product p join Store s on p.store = s and s.isDeleted = false
    where s.id in (:ids)
        and p.isDeleted = false
        and p.status = 'SALE'
        and (:category is null or s.category = :category)
        and (:keyword is null or p.name like concat('%', :keyword, '%'))
        and (:lastId is null or p.id > :lastId)
    """)
    List<ProductSaleDto> findByCond(@Param("ids")List<Long> ids,
                                    @Param("keyword")String keyword,
                                    @Param("category")StoreCategory category,
                                    @Param("lastId")Long lastId);
}
