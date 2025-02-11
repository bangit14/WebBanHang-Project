package com.bang.WebBanHang_Project.service;

import com.bang.WebBanHang_Project.controller.request.CategoryCreation;
import com.bang.WebBanHang_Project.controller.request.CategoryUpdate;
import com.bang.WebBanHang_Project.controller.response.CategoryResponse;
import com.bang.WebBanHang_Project.controller.response.ProductResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse findById(Long id);

    List<ProductResponse> findByCategoryId(Long id);

    long create(CategoryCreation request);

    void update(CategoryUpdate request);

    void delete(long id);
}
