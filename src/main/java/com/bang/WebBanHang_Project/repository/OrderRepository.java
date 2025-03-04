package com.bang.WebBanHang_Project.repository;

import com.bang.WebBanHang_Project.common.OrderStatus;
import com.bang.WebBanHang_Project.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("select o from Order o where lower(o.status) like :keyword" +
            "or lower(o.payment_method) like :keyword")
    Page<Order> searchByKeyword(String keyword, Pageable pageable);

    List<Order> findByUserId(Long userId);

    List<Order> findByStatus(OrderStatus status);

    Optional<Order> findByIdAndUserId(Long orderId, Long userId);
}
