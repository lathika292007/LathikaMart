package com.lathika.lathikamart.dao;

import com.lathika.lathikamart.model.AuditLog;

import java.util.List;

/**
 * Data Access Object interface for AuditLog operations.
 */
public interface AuditLogDAO {
    AuditLog create(AuditLog log);
    List<AuditLog> findAll();
    List<AuditLog> findByAdminId(Long adminId);
}
