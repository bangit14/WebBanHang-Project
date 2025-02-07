package com.bang.WebBanHang_Project.controller;

import com.bang.WebBanHang_Project.controller.request.ProductCreationRequest;
import com.bang.WebBanHang_Project.controller.request.ProductUpdateRequest;
import com.bang.WebBanHang_Project.controller.response.ApiResponse;
import com.bang.WebBanHang_Project.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/product")
@Tag(name = "Product-Service")
@Slf4j(topic = "PRODUCT-CONTROLLER")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create Product", description = "API add new product to database")
    @GetMapping("/{productId}")
    public ApiResponse getProductDetail(@PathVariable Long productId){
        log.info("Get product detail by ID: {}", productId);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("product")
                .data(productService.findById(productId)).build();
    }


    @Operation(summary = "Create Product", description = "API add new product to database")
    @PostMapping("/create")
    public ApiResponse createUser(@RequestBody ProductCreationRequest request){
        log.info("Create Product: {}", request);

        return ApiResponse.builder()
               .status(HttpStatus.CREATED.value())
               .message("Product created successfully")
               .data(productService.createProduct(request)).build();
    }

    @Operation(summary = "Update Product", description = "API update product to database")
    @PutMapping("/upd")
    public ApiResponse updateProduct(@RequestBody ProductUpdateRequest request){
        log.info("Update product: {}", request);

        productService.updateProduct(request);

        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("Product updated successfully")
                .data("").build();
    }

    @Operation(summary = "Delete Product", description = "API delete product")
    @PutMapping("/del")
    public ApiResponse deleteProduct(@PathVariable Long productId){
        log.info("Deleting product: {}", productId);

        productService.deleteProduct(productId);

        return ApiResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Product deleted successfully")
                .data("")
                .build();
    }
}
