package com.bang.WebBanHang_Project.controller;

import com.bang.WebBanHang_Project.common.OrderStatus;
import com.bang.WebBanHang_Project.controller.request.OrderItemDelRequest;
import com.bang.WebBanHang_Project.controller.request.OrderItemRequest;
import com.bang.WebBanHang_Project.controller.request.OrderRequest;
import com.bang.WebBanHang_Project.controller.response.ApiResponse;
import com.bang.WebBanHang_Project.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Slf4j(topic = "ORDER-CONTROLLER")
@Tag(name = "Order-Controller")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Get Order", description = "API get order by id from to database")
    @GetMapping("/{orderId}")
    public ApiResponse getOrder(@PathVariable Long orderId){
        log.info("Get order detail by Id: {}", orderId);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get order detail")
                .data(orderService.getOrder(orderId)).build();
    }

    @Operation(summary = "Create Order", description = "API create new order to database")
    @PostMapping("/create")
    public ApiResponse createOrder(@RequestBody OrderRequest request){
        log.info("Create order: {}", request);

        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Order created successfully")
                .data(orderService.createOrder(request)).build();
    }

    @Operation(summary = "Update Order", description = "API update order to database")
    @PutMapping("/upd")
    public ApiResponse updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatus status){
        log.info("Update order status");

        orderService.updateOrderStatus(orderId,status);

        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("Order status updated successfully")
                .data("").build();
    }

    @Operation(summary = "Add Item", description = "API add item to order")
    @PutMapping("/{orderId}/addItem")
    public ApiResponse addItem(@PathVariable Long orderId, @RequestBody OrderItemRequest request){
        log.info("Add item to Order");

        orderService.addOrderItem(orderId,request);

        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Add item successfully")
                .data("").build();
    }

    @Operation(summary = "Remove Item", description = "API remove item to order")
    @PutMapping("/removeItem")
    public ApiResponse removeItem(@RequestBody OrderItemDelRequest request){
        log.info("Remove item");

        orderService.removeOrderItem(request);

        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("Remove item successfully")
                .data("").build();
    }

    @Operation(summary = "Cancel Order", description = "API cancel order")
    @DeleteMapping("/cancel/{orderId}")
    public ApiResponse cancelOrder(@PathVariable Long orderId){
        log.info("Cancel order");

        orderService.cancelOrder(orderId);

        return ApiResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Cancel order successfully")
                .data("").build();
    }
}
