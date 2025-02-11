package com.bang.WebBanHang_Project.controller;

import com.bang.WebBanHang_Project.controller.request.InventoryCreationRequest;
import com.bang.WebBanHang_Project.controller.request.InventoryUpdateRequest;
import com.bang.WebBanHang_Project.controller.response.ApiResponse;
import com.bang.WebBanHang_Project.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@Slf4j(topic = "INVENTORY-CONTROLLER")
@Tag(name = "Inventory-Service")
@RequiredArgsConstructor
@Validated
public class InventoryController {
    private final InventoryService inventoryService;

    @Operation(summary = "Get Inventory", description = "API get inventory from to database")
    @GetMapping("/{productId}/inventory")
    public ApiResponse getProductInventory(@PathVariable Long productId){
        log.info("Get productInventory");

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Inventory")
                .data(inventoryService.getInventory(productId)).build();
    }

    @Operation(summary = "Create Inventory", description = "API create inventory from to database")
    @PostMapping("/create")
    public ApiResponse createInventory(@RequestBody InventoryCreationRequest request){
        log.info("Create Inventory");

        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Inventory created successfully")
                .data(inventoryService.createInventory(request)).build();
    }

    @Operation(summary = "Update Inventory", description = "API update inventory to database")
    @PutMapping("/upd")
    public ApiResponse updateInventory(@RequestBody InventoryUpdateRequest request){
        log.info("Update Inventory");

        inventoryService.updateInventory(request);

        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("Inventory updated successfully")
                .data("").build();
    }

}
