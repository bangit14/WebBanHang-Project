package com.bang.WebBanHang_Project.service.impl;

import com.bang.WebBanHang_Project.common.OrderStatus;
import com.bang.WebBanHang_Project.common.PaymentStatus;
import com.bang.WebBanHang_Project.common.RefundStatus;
import com.bang.WebBanHang_Project.controller.request.OrderItemRequest;
import com.bang.WebBanHang_Project.controller.request.OrderRequest;
import com.bang.WebBanHang_Project.controller.request.RefundRequest;
import com.bang.WebBanHang_Project.controller.response.OrderItemResponse;
import com.bang.WebBanHang_Project.controller.response.OrderResponse;
import com.bang.WebBanHang_Project.exception.InsufficientInventoryException;
import com.bang.WebBanHang_Project.exception.InventoryServiceException;
import com.bang.WebBanHang_Project.exception.OrderCancellationException;
import com.bang.WebBanHang_Project.exception.ResourceNotFoundException;
import com.bang.WebBanHang_Project.model.Order;
import com.bang.WebBanHang_Project.model.OrderItem;
import com.bang.WebBanHang_Project.model.ProductEntity;
import com.bang.WebBanHang_Project.repository.OrderItemRepository;
import com.bang.WebBanHang_Project.repository.OrderRepository;
import com.bang.WebBanHang_Project.repository.ProductRepository;
import com.bang.WebBanHang_Project.service.InventoryService;
import com.bang.WebBanHang_Project.service.OrderService;
import com.bang.WebBanHang_Project.service.PaymentService;
import jakarta.xml.bind.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j(topic = "ORDER-SERVICE")
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private final ProductRepository productRepository;
    private final PaymentService paymentService;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(OrderRequest orderRequest) {
        try{

            validateOrderRequest(orderRequest);

            checkAndReserveInventory(orderRequest.getOrderItem());

            Order order = new Order();
            order.setUserId(orderRequest.getUserId());
            order.setOrderDate(LocalDateTime.now());
            order.setStatus(orderRequest.getStatus());
            order.setShippingAddress(orderRequest.getShippingAddress());
            order.setPaymentMethod(orderRequest.getPaymentMethod());

            BigDecimal totalAmount = BigDecimal.ZERO;
            List<OrderItem> orderItems = new ArrayList<>();

            for (OrderItemRequest itemRequest : orderRequest.getOrderItem()){
                OrderItem item = createOrderItem(order,itemRequest);
                orderItems.add(item);
                totalAmount = totalAmount.add(item.getSubtotal());
            }

            order.setTotalAmount(totalAmount);
            order.setOrderItems(orderItems);
            orderRepository.save(order);

//            processPayment(order);

            return order.getId().toString();

        } catch (Exception e){
            log.error("Error creating order: ", e);
        }
        return "0";
    }

    @Override
    public OrderResponse getOrder(Long orderId) {
        log.info("Get Order by orderId");

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order not found")
        );

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setUserId(order.getUserId());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setOrderDate(order.getOrderDate());
        orderResponse.setShippingAddress(order.getShippingAddress());
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setPaymentMethod(order.getPaymentMethod());

        List<OrderItemResponse> orderItemResponseList = order.getOrderItems().stream().map(
                orderItem -> OrderItemResponse.builder()
                        .productId(orderItem.getProductId())
                        .price(orderItem.getPrice())
                        .quantity(orderItem.getQuantity())
                        .subtotal(orderItem.getSubtotal()).build()
                ).toList();

        orderResponse.setOrderItem(orderItemResponseList);

        return orderResponse;
    }

    @Override
    public List<OrderResponse> getUserOrder(Long userId) {
        return List.of();
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        log.info("Update OrderStatus");

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        log.info("Starting order cancellation process for orderId: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if(!canCancelOrder(order)){
            log.warn("Cannot cancel order {} in status {}", order.getId(),order.getStatus());
            throw new OrderCancellationException("Cannot cancel order in current status: " + order.getStatus());
        }

        try {

            releaseInventory(order.getOrderItems());

            if(hasPaymentBeenProcessed(order)){
                refundPayment(order);
            }

            order.setStatus(OrderStatus.CANCELLED);
            order.setCancelledAt(LocalDateTime.now());
            order.setCancellationReason("Customer request cancellation");

            orderRepository.save(order);
            log.info("Successfully cancelled order: {}", orderId);
        } catch (Exception e){
            log.error("Error during order cancellation for orderId {}: {}", orderId,e.getMessage());
            throw new OrderCancellationException("Failed to cancel order",e);
        }

    }

    @Override
    public void addOrderItem(Long orderId, OrderItemRequest request) {
        log.info("Add orderItem to order");

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order not found"));

        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(request.getProductId());
        orderItem.setQuantity(request.getQuantity());
        orderItem.setPrice(request.getPrice());
        orderItem.setSubtotal(request.getSubtotal());
        orderItemRepository.save(orderItem);

        List<OrderItem> itemList = order.getOrderItems();
        itemList.add(orderItem);
        order.setOrderItems(itemList);
        orderRepository.save(order);
    }

    @Override
    public void removeOrderItem(Long orderId, Long itemId) {
        log.info("Remove orderItem");

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order not found"));

        OrderItem orderItem = orderItemRepository.findById(itemId).orElseThrow(
                () -> new ResourceNotFoundException("OrderItem not found"));

        orderItemRepository.delete(orderItem);

        List<OrderItem> itemList = order.getOrderItems();
        itemList.remove(orderItem);
        orderRepository.save(order);
    }

    private Order getOrderById(Long orderId){
        return orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    private void validateOrderRequest(OrderRequest orderRequest) throws ValidationException {
        if(orderRequest.getUserId() == null){
            throw new ValidationException("User ID is required");
        }
        if (orderRequest.getOrderItem() == null || orderRequest.getOrderItem().isEmpty()){
            throw new ValidationException("Order must contain at least one item");
        }
    }

    private void checkAndReserveInventory(List<OrderItemRequest> items){
        for (OrderItemRequest item : items){
            boolean reserved = inventoryService.reserveStock(
                    item.getProductId(), item.getQuantity()
            );
            if(!reserved){
                throw new InsufficientInventoryException("Insufficient inventory for product: " + item.getProductId());
            }
        }
    }

    private OrderItem createOrderItem(Order order, OrderItemRequest itemRequest){

        ProductEntity product = productRepository.getById(itemRequest.getProductId());

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProductId(itemRequest.getProductId());
        item.setQuantity(itemRequest.getQuantity());
        item.setPrice(itemRequest.getPrice());
        item.setSubtotal(product.getPrice().multiply(new BigDecimal(itemRequest.getQuantity())));

        return item;
    }

    private boolean canCancelOrder(Order order){

        Set<OrderStatus> cancellableStatuses = Set.of(
                OrderStatus.PENDING,
                OrderStatus.CONFIRMED,
                OrderStatus.PAYMENT_PENDING);

        if (!cancellableStatuses.contains(order.getStatus())){
            return false;
        }

        LocalDateTime orderTime = order.getOrderDate();
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(currentTime,orderTime);

        return duration.toHours() <= 24;
    }

    private void releaseInventory(List<OrderItem> orderItems){
        log.info("Releasing inventory for order items");

        for (OrderItem item : orderItems){
            try{
                inventoryService.releaseStock(
                        item.getProductId(), item.getQuantity());
                log.debug("Released inventory for product: {}, quantity: {}",
                        item.getProductId(), item.getQuantity());
            } catch (InventoryServiceException e){
                log.error("Failed to release inventory for product: {}", item.getProductId());
            }
        }
    }

    private boolean hasPaymentBeenProcessed(Order order){
        try {
            PaymentStatus paymentStatus = paymentService.checkPaymentStatus(order.getPaymentId());

            return paymentStatus == PaymentStatus.COMPLETED ||
                    paymentStatus == PaymentStatus.SETTLED;
        } catch (Exception e) {
            log.error("Error checking payment status for order: {}", order.getId());
        }
        return false;
    }

    private void refundPayment(Order order){
        log.info("Processing refund for order: {}", order.getId());

        try{
            RefundRequest refundRequest = RefundRequest.builder()
                    .orderId(order.getId().toString())
                    .paymentId(order.getPaymentId())
                    .amount(order.getTotalAmount())
                    .reason("Order Cancelled")
                    .build();

            paymentService.refundPayment(order.getPaymentId());

            refundRequest.setStatus(RefundStatus.COMPLETED);
            refundRequest.setRefundDate(LocalDateTime.now());

            log.info("Successfully processed refund for order: {}", order.getId());
        } catch (Exception e) {
            log.error("Failed to process refund for order: {}", order.getId());
        }
    }
}
