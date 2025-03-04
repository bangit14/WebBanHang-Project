package com.bang.WebBanHang_Project.repository;

import com.bang.WebBanHang_Project.model.CategoryAttribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryAttributeRepository extends JpaRepository<CategoryAttribute,Long> {

    CategoryAttribute findByIdAndCategoryId(Long id,Long categoryId);

    CategoryAttribute findByName(String name);

    List<CategoryAttribute> getAttributeByCategoryIdAndName(Long categoryId, String name);

    Page<CategoryAttribute> findByCategoryId(Long categoryId, Pageable pageable);
}
