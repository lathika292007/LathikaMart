package com.lathika.lathikamart.model;

import java.sql.Timestamp;

/**
 * AuditLog model class representing administrative governance events.
 */
public class AuditLog {
    private Long id;
    private Long adminId;
    private String adminEmail;
    private String action;
    private String targetType;
    private Long targetId;
    private String details;
    private Timestamp createdAt;

    public AuditLog() {
    }

    public AuditLog(Long adminId, String adminEmail, String action, String targetType, Long targetId, String details) {
        this.adminId = adminId;
        this.adminEmail = adminEmail;
        this.action = action;
        this.targetType = targetType;
        this.targetId = targetId;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
