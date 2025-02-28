package com.bang.WebBanHang_Project.service;

import com.bang.WebBanHang_Project.common.OrderStatus;
import com.bang.WebBanHang_Project.controller.request.OrderItemDelRequest;
import com.bang.WebBanHang_Project.controller.request.OrderItemRequest;
import com.bang.WebBanHang_Project.controller.request.OrderRequest;
import com.bang.WebBanHang_Project.controller.response.OrderResponse;

import java.util.List;

public interface OrderService {

    long createOrder(OrderRequest orderRequest);

    OrderResponse getOrder(Long orderId);

    List<OrderResponse> getUserOrder(Long userId);

    void updateOrderStatus(Long orderId, OrderStatus status);

    void cancelOrder(Long orderId);

    void addOrderItem(Long orderId, OrderItemRequest request);

    void removeOrderItem(OrderItemDelRequest request);
}
