package com.lathika.lathikamart.dao;

import com.lathika.lathikamart.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Product operations.
 */
public interface ProductDAO {
    Product create(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    List<Product> findByCategory(String category);
    List<Product> search(String keyword, String category);
    List<Product> findWithFilters(String keyword, String category, java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice, String sortBy);
    List<Product> findBySellerId(Long sellerId);
    boolean update(Product product);
    boolean delete(Long id);
}
