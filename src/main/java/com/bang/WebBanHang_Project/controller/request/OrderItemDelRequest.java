package com.bang.WebBanHang_Project.controller.request;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderItemDelRequest {
    private Long orderId;
    private Long productId;
    private Long quantity;
}
