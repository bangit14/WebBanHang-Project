package com.bang.WebBanHang_Project.exception;

public class PaymentProviderNotFoundException extends RuntimeException {
    public PaymentProviderNotFoundException(String message) {
        super(message);
    }
}
