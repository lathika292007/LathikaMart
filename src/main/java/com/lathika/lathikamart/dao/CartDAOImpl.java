package com.lathika.lathikamart.dao;

import com.lathika.lathikamart.listener.DbContextListener;
import com.lathika.lathikamart.model.CartItem;
import com.lathika.lathikamart.model.Product;
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
import java.util.Optional;

/**
 * JDBC implementation of CartDAO using PreparedStatement and try-with-resources.
 */
public class CartDAOImpl implements CartDAO {

    private static final Logger logger = LoggerFactory.getLogger(CartDAOImpl.class);
    private final DataSource dataSource;

    public CartDAOImpl() {
        this.dataSource = DbContextListener.getDataSource();
    }

    public CartDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public CartItem addOrUpdateItem(Long userId, Long productId, int quantity) {
        Optional<CartItem> existingOpt = findByUserIdAndProductId(userId, productId);
        if (existingOpt.isPresent()) {
            CartItem existing = existingOpt.get();
            int newQty = existing.getQuantity() + quantity;
            updateQuantity(existing.getId(), newQty);
            existing.setQuantity(newQty);
            return existing;
        }

        String sql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, productId);
            stmt.setInt(3, quantity);

            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    Long id = keys.getLong(1);
                    return new CartItem(id, userId, productId, quantity);
                }
            }
        } catch (SQLException e) {
            logger.error("Error adding item to cart for user {}", userId, e);
        }
        return new CartItem(null, userId, productId, quantity);
    }

    @Override
    public List<CartItem> findByUserId(Long userId) {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT c.id, c.user_id, c.product_id, c.quantity, c.created_at, " +
                     "p.name, p.description, p.price, p.stock_qty, p.category, p.image_url " +
                     "FROM cart_items c " +
                     "JOIN products p ON c.product_id = p.id " +
                     "WHERE c.user_id = ? ORDER BY c.id ASC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getLong("id"));
                    item.setUserId(rs.getLong("user_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setCreatedAt(rs.getTimestamp("created_at"));

                    Product p = new Product();
                    p.setId(rs.getLong("product_id"));
                    p.setName(rs.getString("name"));
                    p.setDescription(rs.getString("description"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setStockQty(rs.getInt("stock_qty"));
                    p.setCategory(rs.getString("category"));
                    p.setImageUrl(rs.getString("image_url"));
                    item.setProduct(p);

                    items.add(item);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding cart items for user {}", userId, e);
        }
        return items;
    }

    @Override
    public Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId) {
        String sql = "SELECT id, user_id, product_id, quantity, created_at FROM cart_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CartItem item = new CartItem(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getLong("product_id"),
                        rs.getInt("quantity")
                    );
                    item.setCreatedAt(rs.getTimestamp("created_at"));
                    return Optional.of(item);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding cart item for user {} product {}", userId, productId, e);
        }
        return Optional.empty();
    }

    @Override
    public boolean updateQuantity(Long cartItemId, int quantity) {
        if (quantity <= 0) {
            return removeItem(cartItemId);
        }
        String sql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantity);
            stmt.setLong(2, cartItemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating cart item quantity for {}", cartItemId, e);
            return false;
        }
    }

    @Override
    public boolean removeItem(Long cartItemId) {
        String sql = "DELETE FROM cart_items WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, cartItemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting cart item {}", cartItemId, e);
            return false;
        }
    }

    @Override
    public boolean clearCart(Long userId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            return stmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            logger.error("Error clearing cart for user {}", userId, e);
            return false;
        }
    }
}
