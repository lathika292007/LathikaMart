package com.lathika.lathikamart.dto;

/**
 * Standard API Response Envelope per specification Section 13.
 */
public class ApiResponseDTO {
    private boolean success;
    private Object data;
    private ErrorDetails error;

    public ApiResponseDTO() {
    }

    public ApiResponseDTO(boolean success, Object data, ErrorDetails error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static ApiResponseDTO success(Object data) {
        return new ApiResponseDTO(true, data, null);
    }

    public static ApiResponseDTO error(String code, String message) {
        return new ApiResponseDTO(false, null, new ErrorDetails(code, message));
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ErrorDetails getError() {
        return error;
    }

    public void setError(ErrorDetails error) {
        this.error = error;
    }

    public static class ErrorDetails {
        private String code;
        private String message;

        public ErrorDetails() {
        }

        public ErrorDetails(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
