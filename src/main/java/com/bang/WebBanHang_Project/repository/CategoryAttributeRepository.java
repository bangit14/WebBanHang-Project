package com.bang.WebBanHang_Project.repository;

import com.bang.WebBanHang_Project.model.CategoryAttribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryAttributeRepository extends JpaRepository<CategoryAttribute,Long> {
    boolean existsByCategoryIdAndName(Long categoryId,String name);

    boolean existsByCategoryIdAndNameAndIdNot(Long categoryId, String name, Long attributeId);

    Page<CategoryAttribute> findByCategoryId(Long categoryId, Pageable pageable);
}
