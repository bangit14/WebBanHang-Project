package com.bang.WebBanHang_Project.model;

import com.bang.WebBanHang_Project.common.RefundStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_refund")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Refund {
    private String id;
    private String paymentId;
    private BigDecimal amount;
    private RefundStatus status;
    private String gatewayRefundId;
    private LocalDateTime refundedAt;
}
