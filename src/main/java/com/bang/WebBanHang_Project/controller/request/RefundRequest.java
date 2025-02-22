package com.bang.WebBanHang_Project.controller.request;

import com.bang.WebBanHang_Project.common.RefundStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RefundRequest {
    private String orderId;
    private Long paymentId;
    private BigDecimal amount;
    private String reason;
    private RefundStatus status;
    private LocalDateTime refundDate;
}
