package com.bang.WebBanHang_Project.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemRequest {
    private Long productId;
    private Long quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}
