package com.bang.WebBanHang_Project.service;

import com.bang.WebBanHang_Project.controller.request.InventoryCreationRequest;
import com.bang.WebBanHang_Project.controller.request.InventoryUpdateRequest;
import com.bang.WebBanHang_Project.controller.response.InventoryResponse;

public interface InventoryService {

    InventoryResponse getInventory(Long id);

    long createInventory(InventoryCreationRequest request);

    void updateInventory(InventoryUpdateRequest request);

}
