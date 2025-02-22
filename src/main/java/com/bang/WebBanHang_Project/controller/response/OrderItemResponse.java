package com.bang.WebBanHang_Project.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class OrderItemResponse {
    private Long productId;
    private Long quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}
