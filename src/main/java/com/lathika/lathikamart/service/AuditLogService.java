package com.lathika.lathikamart.service;

import com.lathika.lathikamart.model.AuditLog;

import java.util.List;

/**
 * Service interface for managing audit logs.
 */
public interface AuditLogService {
    AuditLog logAction(Long adminId, String adminEmail, String action, String targetType, Long targetId, String details);
    List<AuditLog> getAllAuditLogs();
}
