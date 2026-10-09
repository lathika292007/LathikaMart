package com.lathika.lathikamart;

import com.lathika.lathikamart.exception.ValidationException;
import com.lathika.lathikamart.util.PasswordUtil;
import com.lathika.lathikamart.util.ValidationUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests verifying security auditing rules, XSS escaping, and input validation bounds.
 */
public class SecurityAuditTest {

    @Test
    public void testPasswordHashingSecurity() {
        String rawPassword = "securePassword123!";
        String hash1 = PasswordUtil.hashPassword(rawPassword);
        String hash2 = PasswordUtil.hashPassword(rawPassword);

        // BCrypt uses random salt per hash
        assertNotEquals(hash1, hash2);
        assertTrue(PasswordUtil.checkPassword(rawPassword, hash1));
        assertTrue(PasswordUtil.checkPassword(rawPassword, hash2));
        assertFalse(PasswordUtil.checkPassword("wrongPassword", hash1));
    }

    @Test
    public void testXssSanitization() {
        String maliciousInput = "<script>alert('xss');</script>";
        String sanitized = ValidationUtil.escapeHtml(maliciousInput);

        assertNotNull(sanitized);
        assertFalse(sanitized.contains("<script>"));
        assertEquals("&lt;script&gt;alert(&#x27;xss&#x27;);&lt;/script&gt;", sanitized);
    }

    @Test
    public void testEmailValidationBounds() {
        assertDoesNotThrow(() -> ValidationUtil.validateEmail("user@example.com"));
        assertThrows(ValidationException.class, () -> ValidationUtil.validateEmail("invalid-email"));
        assertThrows(ValidationException.class, () -> ValidationUtil.validateEmail(""));
    }

    @Test
    public void testPasswordLengthValidation() {
        assertDoesNotThrow(() -> ValidationUtil.validatePassword("123456"));
        assertThrows(ValidationException.class, () -> ValidationUtil.validatePassword("12345"));
    }

    @Test
    public void testPriceValidationBounds() {
        assertDoesNotThrow(() -> ValidationUtil.validatePrice(new BigDecimal("10.50")));
        assertThrows(ValidationException.class, () -> ValidationUtil.validatePrice(new BigDecimal("0.00")));
        assertThrows(ValidationException.class, () -> ValidationUtil.validatePrice(new BigDecimal("-5.00")));
    }
}
