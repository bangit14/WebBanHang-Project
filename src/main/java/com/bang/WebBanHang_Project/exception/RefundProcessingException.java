package com.bang.WebBanHang_Project.exception;

import com.bang.WebBanHang_Project.common.RefundStatus;

public class RefundProcessingException extends RuntimeException {
    private final String refundId;
    private final RefundStatus status;

    public RefundProcessingException(String message) {
        super(message);
        this.refundId = null;
        this.status = null;
    }

    public RefundProcessingException(String message, String refundId) {
        super(message);
        this.refundId = refundId;
        this.status = null;
    }

    public RefundProcessingException(String message, String refundId, RefundStatus status) {
        super(message);
        this.refundId = refundId;
        this.status = status;
    }

    public RefundProcessingException(String message, Throwable cause) {
        super(message, cause);
        this.refundId = null;
        this.status = null;
    }

    public String getRefundId() {
        return refundId;
    }

    public RefundStatus getStatus() {
        return status;
    }
}
