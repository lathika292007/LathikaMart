package com.lathika.lathikamart.util;

import com.lathika.lathikamart.exception.ValidationException;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Utility for input sanitization and validation.
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private ValidationUtil() {
    }

    public static void requireNonEmpty(String fieldName, String value) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " is required.");
        }
    }

    public static void validateEmail(String email) throws ValidationException {
        requireNonEmpty("Email", email);
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new ValidationException("Invalid email format.");
        }
    }

    public static void validatePassword(String password) throws ValidationException {
        requireNonEmpty("Password", password);
        if (password.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters long.");
        }
    }

    public static void validatePrice(BigDecimal price) throws ValidationException {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Price must be greater than zero.");
        }
    }

    public static void validatePositiveInt(String fieldName, Integer value) throws ValidationException {
        if (value == null || value <= 0) {
            throw new ValidationException(fieldName + " must be a positive integer.");
        }
    }

    /**
     * Escapes HTML entities to prevent XSS attacks.
     */
    public static String escapeHtml(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
    }
}
