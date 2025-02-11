package com.bang.WebBanHang_Project.controller.request;

import com.bang.WebBanHang_Project.model.ProductEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryCreationRequest {
    private Long id;
    private Long productId;
    private Long quantity;
    private String location;
    private String notes;
    private Long discount;
    private String status;
}
