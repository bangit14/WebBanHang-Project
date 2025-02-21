package com.bang.WebBanHang_Project.service;

import com.bang.WebBanHang_Project.controller.request.CategoryAttributeRequest;
import com.bang.WebBanHang_Project.controller.request.CategoryAttributeUpdate;
import com.bang.WebBanHang_Project.controller.request.CategoryCreation;
import com.bang.WebBanHang_Project.controller.request.CategoryUpdate;
import com.bang.WebBanHang_Project.controller.response.CategoryAttributeResponse;
import com.bang.WebBanHang_Project.controller.response.CategoryResponse;
import com.bang.WebBanHang_Project.controller.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    // Category methods

    CategoryResponse findById(Long id);

    List<ProductResponse> findByCategoryId(Long categoryId);

    Page<CategoryResponse> findCategoryCustom(String keyword, String sort, int page, int size);

    long create(CategoryCreation request);

    void update(CategoryUpdate request);

    void delete(long categoryId);

    // CategoryAttribute methods

    Page<CategoryAttributeResponse> getCategoryAttribute(Long categoryId,Pageable pageable);

    long addAttribute(Long categoryId,CategoryAttributeRequest request);

    void updateAttribute(CategoryAttributeUpdate request);

    void deleteAttribute(long attributeId);
}
