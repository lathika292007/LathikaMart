package com.lathika.lathikamart;

import com.lathika.lathikamart.dao.AuditLogDAO;
import com.lathika.lathikamart.model.AuditLog;
import com.lathika.lathikamart.service.AuditLogService;
import com.lathika.lathikamart.service.AuditLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AuditLogService governance logging.
 */
@ExtendWith(MockitoExtension.class)
public class AuditLogServiceTest {

    @Mock
    private AuditLogDAO auditLogDAO;

    private AuditLogService auditLogService;

    @BeforeEach
    public void setUp() {
        auditLogService = new AuditLogServiceImpl(auditLogDAO);
    }

    @Test
    public void testLogActionSuccess() {
        when(auditLogDAO.create(any(AuditLog.class))).thenAnswer(invocation -> {
            AuditLog log = invocation.getArgument(0);
            log.setId(100L);
            return log;
        });

        AuditLog log = auditLogService.logAction(
                1L, "admin@lathikamart.com", "USER_ROLE_UPDATE", "USER", 5L, "Promoted user to SELLER"
        );

        assertNotNull(log);
        assertEquals(100L, log.getId());
        assertEquals("admin@lathikamart.com", log.getAdminEmail());
        assertEquals("USER_ROLE_UPDATE", log.getAction());
        assertEquals("USER", log.getTargetType());
        assertEquals(5L, log.getTargetId());
        verify(auditLogDAO, times(1)).create(any(AuditLog.class));
    }

    @Test
    public void testGetAllAuditLogs() {
        AuditLog log1 = new AuditLog(1L, "admin@lathikamart.com", "LISTING_REMOVAL", "PRODUCT", 10L, "Deleted violating item");
        when(auditLogDAO.findAll()).thenReturn(List.of(log1));

        List<AuditLog> result = auditLogService.getAllAuditLogs();
        assertEquals(1, result.size());
        assertEquals("LISTING_REMOVAL", result.get(0).getAction());
    }
}
