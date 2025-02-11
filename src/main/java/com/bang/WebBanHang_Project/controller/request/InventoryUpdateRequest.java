package com.bang.WebBanHang_Project.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryUpdateRequest {
    private Long id;
    private Long quantity;
    private String location;
    private String notes;
    private Long discount;
    private String status;
}
