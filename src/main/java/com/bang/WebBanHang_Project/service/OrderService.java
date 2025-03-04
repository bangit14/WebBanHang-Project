package com.bang.WebBanHang_Project.service;

import com.bang.WebBanHang_Project.common.OrderStatus;
import com.bang.WebBanHang_Project.controller.request.OrderItemDelRequest;
import com.bang.WebBanHang_Project.controller.request.OrderItemRequest;
import com.bang.WebBanHang_Project.controller.request.OrderRequest;
import com.bang.WebBanHang_Project.controller.response.OrderPageResponse;
import com.bang.WebBanHang_Project.controller.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderPageResponse getList(String keyword, String sort, int page, int size);

    long createOrder(OrderRequest orderRequest);

    OrderResponse getOrder(Long orderId);

    void updateOrderStatus(Long orderId, OrderStatus status);

    void cancelOrder(Long orderId);

    void addOrderItem(Long orderId, OrderItemRequest request);

    void removeOrderItem(OrderItemDelRequest request);
}
