package com.bang.WebBanHang_Project.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
public class RefundGatewayRequest {
    private String merchantId;
    private String transactionId;
    private BigDecimal amount;
    private String reason;
    private long timestamp;
    private String signature;

}
