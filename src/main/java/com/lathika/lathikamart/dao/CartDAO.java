package com.lathika.lathikamart.dao;

import com.lathika.lathikamart.model.CartItem;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for CartItem operations.
 */
public interface CartDAO {
    CartItem addOrUpdateItem(Long userId, Long productId, int quantity);
    List<CartItem> findByUserId(Long userId);
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);
    boolean updateQuantity(Long cartItemId, int quantity);
    boolean removeItem(Long cartItemId);
    boolean clearCart(Long userId);
}
