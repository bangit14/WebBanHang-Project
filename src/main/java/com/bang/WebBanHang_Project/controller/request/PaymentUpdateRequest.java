package com.bang.WebBanHang_Project.controller.request;

import com.bang.WebBanHang_Project.common.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentUpdateRequest {
    private String id;
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private PaymentMethod paymentMethod;
    private String gatewayTransactionId;

}
