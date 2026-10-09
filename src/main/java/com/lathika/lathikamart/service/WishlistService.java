package com.lathika.lathikamart.service;

import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.model.Wishlist;
import java.util.List;

public interface WishlistService {
    Wishlist addToWishlist(Long userId, Long productId) throws AppException;
    boolean removeFromWishlist(Long userId, Long productId);
    List<Wishlist> getUserWishlist(Long userId);
    boolean isWishlisted(Long userId, Long productId);
}
