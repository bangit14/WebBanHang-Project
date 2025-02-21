package com.bang.WebBanHang_Project.exception;

public class PaymentGatewayException extends RuntimeException {

    private final String gatewayErrorCode;
    private final String gatewayMessage;

    public PaymentGatewayException(String message) {
        super(message);
        this.gatewayErrorCode = null;
        this.gatewayMessage = null;
    }

    public PaymentGatewayException(String message, String gatewayErrorCode, String gatewayMessage) {
        super(message);
        this.gatewayErrorCode = gatewayErrorCode;
        this.gatewayMessage = gatewayMessage;
    }

    public PaymentGatewayException(String message, Throwable cause) {
        super(message, cause);
        this.gatewayErrorCode = null;
        this.gatewayMessage = null;
    }

    public String getGatewayErrorCode() {
        return gatewayErrorCode;
    }

    public String getGatewayMessage() {
        return gatewayMessage;
    }

}
