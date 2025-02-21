package com.bang.WebBanHang_Project.controller.response;

import com.bang.WebBanHang_Project.common.RefundStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class RefundResponse {
    private String id;
    private String paymentId;
    private BigDecimal amount;
    private RefundStatus status;
    private String gatewayRefundId;
}
