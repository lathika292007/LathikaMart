package com.lathika.lathikamart.dao;

import com.lathika.lathikamart.listener.DbContextListener;
import com.lathika.lathikamart.model.Product;
import com.lathika.lathikamart.model.Wishlist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAOImpl implements WishlistDAO {

    private static final Logger logger = LoggerFactory.getLogger(WishlistDAOImpl.class);
    private final DataSource dataSource;

    public WishlistDAOImpl() {
        this.dataSource = DbContextListener.getDataSource();
    }

    public WishlistDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Wishlist add(Long userId, Long productId) {
        String sql = "MERGE INTO wishlist_items (user_id, product_id) KEY(user_id, product_id) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, productId);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                Long id = (rs.next()) ? rs.getLong(1) : null;
                return new Wishlist(id, userId, productId, new Timestamp(System.currentTimeMillis()));
            }
        } catch (SQLException e) {
            logger.error("Error adding product {} to wishlist for user {}", productId, userId, e);
            return new Wishlist(null, userId, productId, new Timestamp(System.currentTimeMillis()));
        }
    }

    @Override
    public boolean remove(Long userId, Long productId) {
        String sql = "DELETE FROM wishlist_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error removing product {} from wishlist for user {}", productId, userId, e);
            return false;
        }
    }

    @Override
    public List<Wishlist> findByUserId(Long userId) {
        String sql = "SELECT w.id AS wishlist_id, w.user_id, w.product_id, w.created_at, " +
                     "p.id AS p_id, p.seller_id, p.name, p.description, p.price, p.stock_qty, p.category, p.image_url " +
                     "FROM wishlist_items w JOIN products p ON w.product_id = p.id " +
                     "WHERE w.user_id = ? ORDER BY w.created_at DESC";
        List<Wishlist> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Wishlist w = new Wishlist();
                    w.setId(rs.getLong("wishlist_id"));
                    w.setUserId(rs.getLong("user_id"));
                    w.setProductId(rs.getLong("product_id"));
                    w.setCreatedAt(rs.getTimestamp("created_at"));

                    Product p = new Product();
                    p.setId(rs.getLong("p_id"));
                    p.setSellerId(rs.getLong("seller_id"));
                    p.setName(rs.getString("name"));
                    p.setDescription(rs.getString("description"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setStockQty(rs.getInt("stock_qty"));
                    p.setCategory(rs.getString("category"));
                    p.setImageUrl(rs.getString("image_url"));

                    w.setProduct(p);
                    list.add(w);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding wishlist items for user {}", userId, e);
        }
        return list;
    }

    @Override
    public boolean exists(Long userId, Long productId) {
        String sql = "SELECT COUNT(*) FROM wishlist_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.error("Error checking wishlist status for user {} product {}", userId, productId, e);
            return false;
        }
    }
}
