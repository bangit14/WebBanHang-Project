package com.bang.WebBanHang_Project.exception;

import com.bang.WebBanHang_Project.common.PaymentStatus;

public class PaymentProcessingException extends RuntimeException {

  private final String transactionId;
  private final PaymentStatus status;

  public PaymentProcessingException(String message) {
    super(message);
    this.transactionId = null;
    this.status = null;
  }

  public PaymentProcessingException(String message, String transactionId) {
    super(message);
    this.transactionId = transactionId;
    this.status = null;
  }

  public PaymentProcessingException(String message, String transactionId, PaymentStatus status) {
    super(message);
    this.transactionId = transactionId;
    this.status = status;
  }

  public PaymentProcessingException(String message, Throwable cause) {
    super(message, cause);
    this.transactionId = null;
    this.status = null;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public PaymentStatus getStatus() {
    return status;
  }

}
