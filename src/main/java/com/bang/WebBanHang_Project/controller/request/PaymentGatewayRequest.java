package com.bang.WebBanHang_Project.controller.request;

import com.bang.WebBanHang_Project.common.PaymentMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class PaymentGatewayRequest {
    private String merchantId;
    private BigDecimal amount;
    private String currency;
    private String orderId;
    private PaymentMethod paymentMethod;
    private String returnUrl;
    private String notifyUrl;
    private long timestamp;
    private String signature;
}


