package com.bang.WebBanHang_Project.controller.response;

import com.bang.WebBanHang_Project.common.OrderStatus;
import com.bang.WebBanHang_Project.common.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long userId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private String shippingAddress;
    private PaymentMethod paymentMethod;
    private List<OrderItemResponse> orderItem;
}
