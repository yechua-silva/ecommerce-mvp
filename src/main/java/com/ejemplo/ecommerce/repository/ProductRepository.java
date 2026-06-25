package com.ejemplo.ecommerce.repository;

import com.ejemplo.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    
    @Query("SELECT p FROM Product p WHERE " +
           "(:search = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:categoryId IS NULL OR p.category.id = :categoryId)")
    List<Product> findAllFiltered(@Param("search") String search, @Param("categoryId") Integer categoryId);
    
    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
           "(:search = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:categoryId IS NULL OR p.category.id = :categoryId)")
    List<Product> findAllFilteredActive(@Param("search") String search, @Param("categoryId") Integer categoryId);
    
    List<Product> findByActiveTrue();
}