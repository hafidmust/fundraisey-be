package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    @Query(value = "SELECT * FROM product WHERE id_startup = :id and deleted_at IS NULL", nativeQuery = true)
    public Product getById(@Param("id") Long id);
}
