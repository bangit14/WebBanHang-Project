package com.bang.WebBanHang_Project.repository;

import com.bang.WebBanHang_Project.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
}
