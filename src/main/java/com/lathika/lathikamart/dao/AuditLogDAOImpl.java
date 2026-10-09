package com.lathika.lathikamart.dao;

import com.lathika.lathikamart.listener.DbContextListener;
import com.lathika.lathikamart.model.AuditLog;
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
 * JDBC implementation of AuditLogDAO using PreparedStatement and try-with-resources.
 */
public class AuditLogDAOImpl implements AuditLogDAO {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogDAOImpl.class);
    private final DataSource dataSource;

    public AuditLogDAOImpl() {
        this.dataSource = DbContextListener.getDataSource();
    }

    public AuditLogDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public AuditLog create(AuditLog log) {
        String sql = "INSERT INTO audit_logs (admin_id, admin_email, action, target_type, target_id, details) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, log.getAdminId());
            stmt.setString(2, log.getAdminEmail());
            stmt.setString(3, log.getAction());
            stmt.setString(4, log.getTargetType());
            stmt.setLong(5, log.getTargetId());
            stmt.setString(6, log.getDetails());

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        log.setId(rs.getLong(1));
                    }
                }
            }
            return log;
        } catch (SQLException e) {
            logger.error("Error creating audit log entry", e);
            throw new RuntimeException("Database error creating audit log", e);
        }
    }

    @Override
    public List<AuditLog> findAll() {
        List<AuditLog> logs = new ArrayList<>();
        String sql = "SELECT id, admin_id, admin_email, action, target_type, target_id, details, created_at FROM audit_logs ORDER BY id DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                logs.add(mapResultSetToAuditLog(rs));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving audit logs", e);
        }
        return logs;
    }

    @Override
    public List<AuditLog> findByAdminId(Long adminId) {
        List<AuditLog> logs = new ArrayList<>();
        String sql = "SELECT id, admin_id, admin_email, action, target_type, target_id, details, created_at FROM audit_logs WHERE admin_id = ? ORDER BY id DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, adminId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    logs.add(mapResultSetToAuditLog(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving audit logs for adminId {}", adminId, e);
        }
        return logs;
    }

    private AuditLog mapResultSetToAuditLog(ResultSet rs) throws SQLException {
        AuditLog log = new AuditLog();
        log.setId(rs.getLong("id"));
        log.setAdminId(rs.getLong("admin_id"));
        log.setAdminEmail(rs.getString("admin_email"));
        log.setAction(rs.getString("action"));
        log.setTargetType(rs.getString("target_type"));
        log.setTargetId(rs.getLong("target_id"));
        log.setDetails(rs.getString("details"));
        log.setCreatedAt(rs.getTimestamp("created_at"));
        return log;
    }
}
