package com.bang.WebBanHang_Project.exception;

public class PaymentValidationException extends RuntimeException {
    private final String field;
    private final String value;

    public PaymentValidationException(String message) {
        super(message);
        this.field = null;
        this.value = null;
    }

    public PaymentValidationException(String message, String field, String value) {
        super(message);
        this.field = field;
        this.value = value;
    }

    public PaymentValidationException(String message, Throwable cause) {
        super(message, cause);
        this.field = null;
        this.value = null;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}
