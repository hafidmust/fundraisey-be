package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.Product;
import com.fundraisey.backend.entity.startup.Startup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    @Query(value = "SELECT * FROM product WHERE id_startup = :id and deleted_at IS NULL", nativeQuery = true)
    public Product getByStartupId(@Param("id") Long id);

    @Query("SELECT p FROM Product p WHERE p.id = :id")
    public Product getByProductId(@Param("id") Long id);

    List<Product> findByStartup(Startup startup);
}
