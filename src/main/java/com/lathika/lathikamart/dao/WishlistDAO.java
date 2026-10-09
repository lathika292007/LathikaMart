package com.lathika.lathikamart.dao;

import com.lathika.lathikamart.model.Wishlist;
import java.util.List;

public interface WishlistDAO {
    Wishlist add(Long userId, Long productId);
    boolean remove(Long userId, Long productId);
    List<Wishlist> findByUserId(Long userId);
    boolean exists(Long userId, Long productId);
}
