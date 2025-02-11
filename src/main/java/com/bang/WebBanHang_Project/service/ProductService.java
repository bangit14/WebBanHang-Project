package com.bang.WebBanHang_Project.service;

import com.bang.WebBanHang_Project.controller.request.InventoryCreationRequest;
import com.bang.WebBanHang_Project.controller.request.InventoryUpdateRequest;
import com.bang.WebBanHang_Project.controller.request.ProductCreationRequest;
import com.bang.WebBanHang_Project.controller.request.ProductUpdateRequest;
import com.bang.WebBanHang_Project.controller.response.InventoryResponse;
import com.bang.WebBanHang_Project.controller.response.ProductPageResponse;
import com.bang.WebBanHang_Project.controller.response.ProductResponse;
import com.bang.WebBanHang_Project.controller.response.UserPageResponse;

import java.util.List;

public interface ProductService {

    ProductPageResponse findAll(String keyword, String sort, int page, int size);

    ProductResponse findById(Long id);

    long createProduct(ProductCreationRequest request);

    void updateProduct(ProductUpdateRequest request);

    void deleteProduct(Long id);

}
