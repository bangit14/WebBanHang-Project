package com.bang.WebBanHang_Project.controller.response;

import com.bang.WebBanHang_Project.common.PaymentMethod;
import com.bang.WebBanHang_Project.common.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String gatewayTransactionId;
}
