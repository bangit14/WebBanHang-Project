package com.bang.WebBanHang_Project.controller.request;

import com.bang.WebBanHang_Project.common.OrderStatus;
import com.bang.WebBanHang_Project.common.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private Long userId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private String shippingAddress;
    private PaymentMethod paymentMethod;
    private List<OrderItemRequest> orderItem;
}
