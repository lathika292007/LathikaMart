package com.lathika.lathikamart.service;

import com.lathika.lathikamart.dao.AuditLogDAO;

import com.lathika.lathikamart.model.AuditLog;

import java.util.List;

/**
 * Implementation of AuditLogService.
 */
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogDAO auditLogDAO;

    public AuditLogServiceImpl(AuditLogDAO auditLogDAO) {
        this.auditLogDAO = auditLogDAO;
    }

    @Override
    public AuditLog logAction(Long adminId, String adminEmail, String action, String targetType, Long targetId, String details) {
        AuditLog log = new AuditLog(adminId, adminEmail, action, targetType, targetId, details);
        return auditLogDAO.create(log);
    }

    @Override
    public List<AuditLog> getAllAuditLogs() {
        return auditLogDAO.findAll();
    }
}
