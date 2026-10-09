package com.lathika.lathikamart.exception;

/**
 * Thrown when input validation fails (HTTP 400).
 */
public class ValidationException extends AppException {
    public ValidationException(String message) {
        super(message, 400);
    }
}
