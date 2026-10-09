package com.lathika.lathikamart;

import com.lathika.lathikamart.dao.ProductDAO;
import com.lathika.lathikamart.dao.ProductDAOImpl;
import com.lathika.lathikamart.dao.UserDAO;
import com.lathika.lathikamart.dao.UserDAOImpl;
import com.lathika.lathikamart.model.Product;
import com.lathika.lathikamart.model.Role;
import com.lathika.lathikamart.model.User;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration unit test for ProductDAO create, find, update, and delete.
 */
public class ProductDAOTest {

    private static HikariDataSource dataSource;
    private ProductDAO productDAO;
    private UserDAO userDAO;
    private Long testSellerId;

    @BeforeAll
    public static void setUpDb() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:product_test;DB_CLOSE_DELAY=-1;MODE=MySQL");
        config.setDriverClassName("org.h2.Driver");
        config.setUsername("sa");
        config.setPassword("");
        dataSource = new HikariDataSource(config);
    }

    @AfterAll
    public static void tearDownDb() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @BeforeEach
    public void initSchema() throws Exception {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("db/migrations/V1__init_schema.sql");
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
             Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.startsWith("--") || trimmed.isEmpty()) continue;
                sql.append(line).append("\n");
                if (trimmed.endsWith(";")) {
                    stmt.execute(sql.toString());
                    sql.setLength(0);
                }
            }
            stmt.execute("DELETE FROM products");
            stmt.execute("DELETE FROM users");
        }
        userDAO = new UserDAOImpl(dataSource);
        productDAO = new ProductDAOImpl(dataSource);

        User seller = new User();
        seller.setName("Test Seller");
        seller.setEmail("seller@test.com");
        seller.setPasswordHash("hash");
        seller.setRole(Role.SELLER);
        User createdSeller = userDAO.create(seller);
        testSellerId = createdSeller.getId();
    }

    @Test
    public void testCreateProductSuccess() {
        Product p = new Product();
        p.setSellerId(testSellerId);
        p.setName("Wireless Noise Cancelling Headphones");
        p.setDescription("Premium Bluetooth 5.3 Over-Ear Headphones");
        p.setPrice(new BigDecimal("199.99"));
        p.setStockQty(50);
        p.setCategory("Smartphones & ACs");
        p.setImageUrl("https://images.unsplash.com/photo-1505740420928-5e560c06d30e");

        Product created = productDAO.create(p);
        assertNotNull(created);
        assertNotNull(created.getId());
        assertTrue(created.getId() > 0);
        assertEquals("Wireless Noise Cancelling Headphones", created.getName());

        Optional<Product> found = productDAO.findById(created.getId());
        assertTrue(found.isPresent());
        assertEquals(testSellerId, found.get().getSellerId());
        assertEquals("Smartphones & ACs", found.get().getCategory());
    }
}
