package com.lathika.lathikamart.service;

import com.lathika.lathikamart.dao.ProductDAO;
import com.lathika.lathikamart.dao.ReviewDAO;
import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.exception.NotFoundException;
import com.lathika.lathikamart.exception.ValidationException;
import com.lathika.lathikamart.model.Review;
import com.lathika.lathikamart.util.ValidationUtil;

import java.util.List;

/**
 * Implementation of ReviewService.
 */
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDAO reviewDAO;
    private final ProductDAO productDAO;

    public ReviewServiceImpl(ReviewDAO reviewDAO, ProductDAO productDAO) {
        this.reviewDAO = reviewDAO;
        this.productDAO = productDAO;
    }

    @Override
    public Review addReview(Long userId, Long productId, int rating, String comment) throws AppException {
        if (userId == null || productId == null) {
            throw new ValidationException("User and Product ID are required.");
        }
        if (rating < 1 || rating > 5) {
            throw new ValidationException("Rating must be between 1 and 5 stars.");
        }
        productDAO.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + productId));

        if (!reviewDAO.hasBuyerPurchasedProduct(userId, productId)) {
            throw new ValidationException("Reviews can only be submitted for products you have purchased.");
        }

        Review review = new Review();
        review.setUserId(userId);
        review.setProductId(productId);
        review.setRating(rating);
        review.setComment(ValidationUtil.escapeHtml(comment));

        return reviewDAO.create(review);
    }

    @Override
    public List<Review> getProductReviews(Long productId) {
        return reviewDAO.findByProductId(productId);
    }
}
