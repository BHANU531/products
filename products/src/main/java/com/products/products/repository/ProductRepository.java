package com.products.products.repository;

import com.products.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    List<Product> findByQuantityLessThan(Integer quantity);

    List<Product> findTop10ByOrderByPurchaseQuantityDesc();

    List<Product> findByIsFeaturedTrue();

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findTop10ByOrderByCreatedDateDesc();

    List<Product> findByIsActiveTrue();

    List<Product> findByIsActiveFalse();
}