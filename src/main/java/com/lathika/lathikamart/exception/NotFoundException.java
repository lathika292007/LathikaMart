package com.lathika.lathikamart.exception;

/**
 * Thrown when requested resource is not found (HTTP 404).
 */
public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super(message, 404);
    }
}
