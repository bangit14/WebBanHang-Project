package com.bang.WebBanHang_Project.controller.request;

import com.bang.WebBanHang_Project.common.PaymentMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class PaymentRequest {
    private String id;
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private PaymentMethod paymentMethod;
    private String gatewayTransactionId;

}
