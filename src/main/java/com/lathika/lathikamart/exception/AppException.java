package com.lathika.lathikamart.exception;

/**
 * Custom base exception for LathikaMart application.
 */
public class AppException extends Exception {
    private final int statusCode;

    public AppException(String message) {
        super(message);
        this.statusCode = 500;
    }

    public AppException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public AppException(String message, Throwable cause, int statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
