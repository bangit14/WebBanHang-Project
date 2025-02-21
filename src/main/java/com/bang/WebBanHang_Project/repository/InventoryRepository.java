package com.bang.WebBanHang_Project.repository;

import com.bang.WebBanHang_Project.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    Inventory findByProductId(Long productId);

}
