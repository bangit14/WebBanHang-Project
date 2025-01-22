package com.bang.WebBanHang_Project.controller;

import com.bang.WebBanHang_Project.controller.request.ProductCreationRequest;
import com.bang.WebBanHang_Project.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody ProductCreationRequest request){
        log.info("Create Product: {}", request);

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("status", org.springframework.http.HttpStatus.CREATED.value());
        result.put("message","Product created successfully");
        result.put("data",productService.createProduct(request));

       return new ResponseEntity<>(result, HttpStatus.CREATED);
    }



}
