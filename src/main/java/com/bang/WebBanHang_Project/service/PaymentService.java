package com.bang.WebBanHang_Project.service;

import com.bang.WebBanHang_Project.common.PaymentProviderType;
import com.bang.WebBanHang_Project.common.PaymentStatus;
import com.bang.WebBanHang_Project.controller.request.PaymentRequest;
import com.bang.WebBanHang_Project.controller.request.PaymentUpdateRequest;
import com.bang.WebBanHang_Project.controller.response.PaymentResponse;

public interface PaymentService {

    PaymentResponse processPayment(PaymentRequest paymentRequest);

    void refundPayment(String paymentId);

    PaymentStatus checkPaymentStatus(String paymentId);

    PaymentProviderType getProviderType();

    String createPaymentRecord(PaymentRequest paymentRequest);

    void updatePaymentRecord(PaymentUpdateRequest paymentUpdateRequest);

    void deletePaymentRecord(String paymentId);
}
