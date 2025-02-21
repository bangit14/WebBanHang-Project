package com.bang.WebBanHang_Project.repository;

import com.bang.WebBanHang_Project.common.OrderStatus;
import com.bang.WebBanHang_Project.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findByStatus(OrderStatus status);

    Optional<Order> findByIdAndUserId(Long orderId, Long userId);
}
