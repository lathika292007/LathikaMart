package com.lathika.lathikamart.service;

import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.model.Product;

import java.util.List;

/**
 * Service interface for product listing management.
 */
public interface ProductService {
    Product createProduct(Product product) throws AppException;
    Product getProductById(Long id) throws AppException;
    List<Product> getAllProducts();
    List<Product> searchProducts(String keyword, String category);
    List<Product> searchProductsWithFilters(String keyword, String category, java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice, String sortBy);
    List<Product> getProductsBySeller(Long sellerId);
    Product updateProduct(Product product, Long requestingUserId, boolean isAdmin) throws AppException;
    boolean deleteProduct(Long id, Long requestingUserId, boolean isAdmin) throws AppException;
}
