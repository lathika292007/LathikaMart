package com.lathika.lathikamart.service;

import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.model.CartItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for Cart operations.
 */
public interface CartService {
    CartItem addToCart(Long userId, Long productId, int quantity) throws AppException;
    List<CartItem> getCartItems(Long userId);
    BigDecimal getCartTotal(Long userId);
    boolean updateQuantity(Long cartItemId, int quantity, Long userId) throws AppException;
    boolean removeFromCart(Long cartItemId, Long userId) throws AppException;
    boolean clearCart(Long userId);
}
