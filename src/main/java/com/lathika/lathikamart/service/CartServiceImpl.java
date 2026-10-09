package com.lathika.lathikamart.service;

import com.lathika.lathikamart.dao.CartDAO;
import com.lathika.lathikamart.dao.ProductDAO;
import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.exception.NotFoundException;
import com.lathika.lathikamart.exception.ValidationException;
import com.lathika.lathikamart.model.CartItem;
import com.lathika.lathikamart.model.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementation of CartService.
 */
public class CartServiceImpl implements CartService {

    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartServiceImpl(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    @Override
    public CartItem addToCart(Long userId, Long productId, int quantity) throws AppException {
        if (userId == null || productId == null) {
            throw new ValidationException("User and Product ID are required.");
        }
        if (quantity <= 0) {
            throw new ValidationException("Quantity must be greater than zero.");
        }

        Product product = productDAO.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + productId));

        if (product.getStockQty() < quantity) {
            throw new ValidationException("Requested quantity exceeds available stock (" + product.getStockQty() + ").");
        }

        return cartDAO.addOrUpdateItem(userId, productId, quantity);
    }

    @Override
    public List<CartItem> getCartItems(Long userId) {
        return cartDAO.findByUserId(userId);
    }

    @Override
    public BigDecimal getCartTotal(Long userId) {
        List<CartItem> items = getCartItems(userId);
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            if (item.getProduct() != null && item.getProduct().getPrice() != null) {
                BigDecimal itemTotal = item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity()));
                total = total.add(itemTotal);
            }
        }
        return total;
    }

    @Override
    public boolean updateQuantity(Long cartItemId, int quantity, Long userId) throws AppException {
        if (cartItemId == null || cartItemId <= 0) {
            throw new ValidationException("Invalid cart item ID.");
        }
        return cartDAO.updateQuantity(cartItemId, quantity);
    }

    @Override
    public boolean removeFromCart(Long cartItemId, Long userId) throws AppException {
        if (cartItemId == null || cartItemId <= 0) {
            throw new ValidationException("Invalid cart item ID.");
        }
        return cartDAO.removeItem(cartItemId);
    }

    @Override
    public boolean clearCart(Long userId) {
        return cartDAO.clearCart(userId);
    }
}
