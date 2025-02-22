package com.bang.WebBanHang_Project.model;

import com.bang.WebBanHang_Project.common.PaymentMethod;
import com.bang.WebBanHang_Project.common.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends AbstractEntity<Long> {

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "status")
    private PaymentStatus status;

    @Column(name = "transactionId")
    private String gatewayTransactionId;

    @Column(name = "processedAt")
    private LocalDateTime processedAt;

    @Column(name = "refund_id")
    private Refund refund;
}
