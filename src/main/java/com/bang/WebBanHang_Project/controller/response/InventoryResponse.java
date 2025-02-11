package com.bang.WebBanHang_Project.controller.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private Long id;
    private Long productId;
    private Long quantity;
    private String location;
    private Long discount;
    private String status;
}
