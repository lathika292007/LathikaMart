package com.lathika.lathikamart.listener;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

/**
 * ServletContextListener that initializes and manages the HikariCP connection pool lifecycle
 * and auto-executes database migrations on application startup.
 */
@WebListener
public class DbContextListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(DbContextListener.class);
    private static HikariDataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Initializing HikariCP DataSource connection pool...");
        try {
            Properties props = new Properties();
            try (InputStream in = DbContextListener.class.getClassLoader().getResourceAsStream("application.properties")) {
                if (in != null) {
                    props.load(in);
                }
            }

            String dbUrl = System.getenv("DB_URL") != null ? System.getenv("DB_URL") : props.getProperty("db.url", "jdbc:h2:mem:lathikamart;DB_CLOSE_DELAY=-1;MODE=MySQL");
            String dbDriver = System.getenv("DB_DRIVER") != null ? System.getenv("DB_DRIVER") : props.getProperty("db.driver", "org.h2.Driver");
            String dbUser = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : props.getProperty("db.user", "sa");
            String dbPassword = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : props.getProperty("db.password", "");

            HikariConfig config = new HikariConfig();
            config.setDriverClassName(dbDriver);
            config.setJdbcUrl(dbUrl);
            config.setUsername(dbUser);
            config.setPassword(dbPassword);

            config.setMaximumPoolSize(Integer.parseInt(props.getProperty("hikari.maximumPoolSize", "10")));
            config.setMinimumIdle(Integer.parseInt(props.getProperty("hikari.minimumIdle", "2")));
            config.setIdleTimeout(Long.parseLong(props.getProperty("hikari.idleTimeout", "30000")));
            config.setConnectionTimeout(Long.parseLong(props.getProperty("hikari.connectionTimeout", "30000")));

            dataSource = new HikariDataSource(config);
            sce.getServletContext().setAttribute("dataSource", dataSource);
            logger.info("HikariCP DataSource initialized successfully with URL: {}", dbUrl);

            // Execute migrations
            runMigrationScript("db/migrations/V1__init_schema.sql");
            runMigrationScript("db/migrations/V2__seed_data.sql");
            runMigrationScript("db/migrations/V3__create_audit_logs.sql");

        } catch (Exception e) {
            logger.error("Failed to initialize HikariCP DataSource", e);
            throw new RuntimeException("Database initialization failure", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Closing HikariCP DataSource connection pool...");
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("HikariCP DataSource successfully closed.");
        }
    }

    public static DataSource getDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource not initialized by DbContextListener");
        }
        return dataSource;
    }

    public static void setDataSource(HikariDataSource ds) {
        dataSource = ds;
    }

    private void runMigrationScript(String scriptPath) {
        logger.info("Running database migration script: {}", scriptPath);
        try (InputStream in = DbContextListener.class.getClassLoader().getResourceAsStream(scriptPath)) {
            if (in == null) {
                logger.warn("Migration script not found on classpath: {}", scriptPath);
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                 Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {

                StringBuilder sqlBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.startsWith("--") || trimmed.isEmpty()) {
                        continue;
                    }
                    sqlBuilder.append(line).append("\n");
                    if (trimmed.endsWith(";")) {
                        String sql = sqlBuilder.toString().trim();
                        if (!sql.isEmpty()) {
                            stmt.execute(sql);
                        }
                        sqlBuilder.setLength(0);
                    }
                }
                logger.info("Executed migration script successfully: {}", scriptPath);
            }
        } catch (Exception e) {
            logger.error("Error executing migration script: {}", scriptPath, e);
        }
    }
}
