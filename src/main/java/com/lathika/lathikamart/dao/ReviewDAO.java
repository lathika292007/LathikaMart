package com.lathika.lathikamart.dao;

import com.lathika.lathikamart.model.Review;

import java.util.List;

/**
 * Data Access Object interface for Product Reviews.
 */
public interface ReviewDAO {
    Review create(Review review);
    List<Review> findByProductId(Long productId);
    boolean hasBuyerPurchasedProduct(Long userId, Long productId);
}
