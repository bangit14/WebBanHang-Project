package com.bang.WebBanHang_Project.exception;


import com.bang.WebBanHang_Project.common.PaymentStatus;

public class RefundNotAllowedException extends PaymentException {

  private final PaymentStatus currentStatus;

  public RefundNotAllowedException(String message) {
    super(message);
    this.currentStatus = null;
  }

  public RefundNotAllowedException(String message, PaymentStatus currentStatus) {
    super(message);
    this.currentStatus = currentStatus;
  }

  public PaymentStatus getCurrentStatus() {
    return currentStatus;
  }
}
