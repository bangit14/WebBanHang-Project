package com.bang.WebBanHang_Project.repository;

import com.bang.WebBanHang_Project.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity,Long> {

    ProductEntity findByName(String name);

    List<ProductEntity> findListProductByName(String name);
}
