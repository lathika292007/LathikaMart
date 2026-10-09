package com.lathika.lathikamart.service;

import com.lathika.lathikamart.dao.ProductDAO;
import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.exception.AuthException;
import com.lathika.lathikamart.exception.NotFoundException;
import com.lathika.lathikamart.exception.ValidationException;
import com.lathika.lathikamart.model.Product;
import com.lathika.lathikamart.util.ValidationUtil;

import java.util.List;

/**
 * Implementation of ProductService with input validation and seller ownership checks.
 */
public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public Product createProduct(Product product) throws AppException {
        if (product == null) {
            throw new ValidationException("Product data cannot be null.");
        }
        ValidationUtil.requireNonEmpty("Product name", product.getName());
        ValidationUtil.requireNonEmpty("Category", product.getCategory());
        ValidationUtil.validatePrice(product.getPrice());
        ValidationUtil.validatePositiveInt("Stock quantity", product.getStockQty());

        if (product.getSellerId() == null || product.getSellerId() <= 0) {
            throw new ValidationException("Invalid seller ID.");
        }

        return productDAO.create(product);
    }

    @Override
    public Product getProductById(Long id) throws AppException {
        if (id == null || id <= 0) {
            throw new ValidationException("Invalid product ID.");
        }
        return productDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    @Override
    public List<Product> searchProducts(String keyword, String category) {
        return productDAO.search(keyword, category);
    }

    @Override
    public List<Product> searchProductsWithFilters(String keyword, String category, java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice, String sortBy) {
        return productDAO.findWithFilters(keyword, category, minPrice, maxPrice, sortBy);
    }

    @Override
    public List<Product> getProductsBySeller(Long sellerId) {
        return productDAO.findBySellerId(sellerId);
    }

    @Override
    public Product updateProduct(Product product, Long requestingUserId, boolean isAdmin) throws AppException {
        if (product == null || product.getId() == null) {
            throw new ValidationException("Invalid product update request.");
        }
        Product existing = getProductById(product.getId());

        if (!isAdmin && !existing.getSellerId().equals(requestingUserId)) {
            throw new AuthException("You do not have permission to edit this product listing.", 403);
        }

        ValidationUtil.requireNonEmpty("Product name", product.getName());
        ValidationUtil.requireNonEmpty("Category", product.getCategory());
        ValidationUtil.validatePrice(product.getPrice());
        ValidationUtil.validatePositiveInt("Stock quantity", product.getStockQty());

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStockQty(product.getStockQty());
        existing.setCategory(product.getCategory());
        existing.setImageUrl(product.getImageUrl());

        productDAO.update(existing);
        return existing;
    }

    @Override
    public boolean deleteProduct(Long id, Long requestingUserId, boolean isAdmin) throws AppException {
        Product existing = getProductById(id);
        if (!isAdmin && !existing.getSellerId().equals(requestingUserId)) {
            throw new AuthException("You do not have permission to delete this product listing.", 403);
        }
        return productDAO.delete(id);
    }
}
