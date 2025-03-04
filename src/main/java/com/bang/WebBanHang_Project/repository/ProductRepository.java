package com.bang.WebBanHang_Project.repository;

import com.bang.WebBanHang_Project.model.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
    @Query(value = "select p from ProductEntity p where lower(p.name) like :keyword ")
    Page<ProductEntity> searchByKeyword(String keyword, Pageable pageable);

    ProductEntity findByName(String name);

    List<ProductEntity> findByCategoryId(Long id);
}
