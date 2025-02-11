package com.bang.WebBanHang_Project.service.impl;

import com.bang.WebBanHang_Project.controller.request.InventoryCreationRequest;
import com.bang.WebBanHang_Project.controller.request.InventoryUpdateRequest;
import com.bang.WebBanHang_Project.controller.response.InventoryResponse;
import com.bang.WebBanHang_Project.exception.ResourceNotFoundException;
import com.bang.WebBanHang_Project.model.Inventory;
import com.bang.WebBanHang_Project.model.ProductEntity;
import com.bang.WebBanHang_Project.repository.InventoryRepository;
import com.bang.WebBanHang_Project.repository.ProductRepository;
import com.bang.WebBanHang_Project.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j(topic = "INVENTORY-SERVICE")
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Override
    public InventoryResponse getInventory(Long inventoryId) {
        log.info("Get Inventory by id: {}", inventoryId);

        Inventory inventory = getInventoryById(inventoryId);

        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setId(inventory.getId());
        inventoryResponse.setProductId(inventory.getProduct().getId());
        inventoryResponse.setQuantity(inventory.getQuantity());
        inventoryResponse.setLocation(inventory.getLocation());
        inventoryResponse.setDiscount(inventory.getDiscount());
        inventoryResponse.setStatus(inventory.getStatus());

        return inventoryResponse;
    }

    @Override
    public long createInventory(InventoryCreationRequest request) {
        log.info("Create Inventory");

        ProductEntity product = getProductById(request.getProductId());

        Inventory inventoryEntity = new Inventory();
        inventoryEntity.setProduct(product);
        inventoryEntity.setQuantity(request.getQuantity());
        inventoryEntity.setLocation(request.getLocation());
        inventoryEntity.setNotes(request.getNotes());
        inventoryEntity.setDiscount(request.getDiscount());
        inventoryEntity.setStatus(request.getStatus());
        inventoryRepository.save(inventoryEntity);
        log.info("Created inventory");

        return inventoryEntity.getId();
    }

    @Override
    public void updateInventory(InventoryUpdateRequest request) {
        log.info("Update Inventory");

        Inventory inventory = getInventoryById(request.getId());
        inventory.setQuantity(request.getQuantity());
        inventory.setLocation(request.getLocation());
        inventory.setNotes(request.getNotes());
        inventory.setDiscount(request.getDiscount());
        inventory.setStatus(request.getStatus());

        inventoryRepository.save(inventory);
        log.info("Updated Inventory");
    }

    private Inventory getInventoryById(Long id){
        return inventoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
    }

    private ProductEntity getProductById(Long id){
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
}
