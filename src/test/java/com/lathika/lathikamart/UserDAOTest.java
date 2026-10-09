package com.lathika.lathikamart;

import com.lathika.lathikamart.dao.UserDAO;
import com.lathika.lathikamart.dao.UserDAOImpl;
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
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 DAO CRUD tests executed against an embedded in-memory H2 instance.
 */
public class UserDAOTest {

    private static HikariDataSource dataSource;
    private UserDAO userDAO;

    @BeforeAll
    public static void setUpDb() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=MySQL");
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
            stmt.execute("DELETE FROM users");
        }
        userDAO = new UserDAOImpl(dataSource);
    }

    @Test
    public void testCreateAndFindUser() {
        User u = new User();
        u.setName("Test Buyer");
        u.setEmail("buyer.test@example.com");
        u.setPasswordHash("hashed_pw_123");
        u.setRole(Role.BUYER);

        User created = userDAO.create(u);
        assertNotNull(created.getId());

        Optional<User> found = userDAO.findByEmail("buyer.test@example.com");
        assertTrue(found.isPresent());
        assertEquals("Test Buyer", found.get().getName());
        assertEquals(Role.BUYER, found.get().getRole());
    }

    @Test
    public void testFindByIdAndDelete() {
        User u = new User();
        u.setName("Delete Me");
        u.setEmail("delete@example.com");
        u.setPasswordHash("hash");
        u.setRole(Role.SELLER);

        User created = userDAO.create(u);
        Long id = created.getId();

        Optional<User> found = userDAO.findById(id);
        assertTrue(found.isPresent());

        boolean deleted = userDAO.delete(id);
        assertTrue(deleted);

        Optional<User> foundAfterDelete = userDAO.findById(id);
        assertFalse(foundAfterDelete.isPresent());
    }
}
