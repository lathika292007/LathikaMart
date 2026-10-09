package com.lathika.lathikamart.service;

import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.model.Review;

import java.util.List;

/**
 * Service interface for product reviews and ratings.
 */
public interface ReviewService {
    Review addReview(Long userId, Long productId, int rating, String comment) throws AppException;
    List<Review> getProductReviews(Long productId);
}
