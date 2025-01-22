package com.bang.WebBanHang_Project.service;

import com.bang.WebBanHang_Project.controller.request.ProductCreationRequest;
import com.bang.WebBanHang_Project.controller.request.ProductUpdateRequest;

public interface ProductService {

    long createProduct(ProductCreationRequest request);

    void updateProduct(ProductUpdateRequest request);

    void deleteProduct(Long id);

}
