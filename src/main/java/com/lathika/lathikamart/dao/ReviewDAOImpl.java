package com.lathika.lathikamart.dao;

import com.lathika.lathikamart.listener.DbContextListener;
import com.lathika.lathikamart.model.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of ReviewDAO using PreparedStatement and try-with-resources.
 */
public class ReviewDAOImpl implements ReviewDAO {

    private static final Logger logger = LoggerFactory.getLogger(ReviewDAOImpl.class);
    private final DataSource dataSource;

    public ReviewDAOImpl() {
        this.dataSource = DbContextListener.getDataSource();
    }

    public ReviewDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Review create(Review review) {
        String sql = "INSERT INTO reviews (product_id, user_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, review.getProductId());
            stmt.setLong(2, review.getUserId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());

            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    review.setId(keys.getLong(1));
                }
            }
            return review;
        } catch (SQLException e) {
            logger.error("Error creating review for product {}", review.getProductId(), e);
            throw new RuntimeException("Database error saving review", e);
        }
    }

    @Override
    public List<Review> findByProductId(Long productId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.id, r.product_id, r.user_id, r.rating, r.comment, r.created_at, u.name AS user_name " +
                     "FROM reviews r JOIN users u ON r.user_id = u.id " +
                     "WHERE r.product_id = ? ORDER BY r.id DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Review rev = new Review();
                    rev.setId(rs.getLong("id"));
                    rev.setProductId(rs.getLong("product_id"));
                    rev.setUserId(rs.getLong("user_id"));
                    rev.setRating(rs.getInt("rating"));
                    rev.setComment(rs.getString("comment"));
                    rev.setCreatedAt(rs.getTimestamp("created_at"));
                    rev.setUserName(rs.getString("user_name"));
                    reviews.add(rev);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding reviews for product {}", productId, e);
        }
        return reviews;
    }

    @Override
    public boolean hasBuyerPurchasedProduct(Long userId, Long productId) {
        String sql = "SELECT COUNT(*) FROM orders o " +
                     "JOIN order_items oi ON o.id = oi.order_id " +
                     "WHERE o.buyer_id = ? AND oi.product_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Error checking purchase history for user {} and product {}", userId, productId, e);
        }
        return false;
    }
}
