package com.bang.WebBanHang_Project.model;

import com.bang.WebBanHang_Project.common.RefundStatus;
import jakarta.persistence.*;
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
public class Refund extends AbstractEntity<Long> {

    @Column(name = "payment_Id")
    private Long paymentId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RefundStatus status;

    @Column(name = "gatewayRefund_Id")
    private String gatewayRefundId;

    @Column(name = "refundedAt")
    private LocalDateTime refundedAt;
}
