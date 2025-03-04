package com.bang.WebBanHang_Project.service;


import com.bang.WebBanHang_Project.controller.request.ProductCreationRequest;
import com.bang.WebBanHang_Project.controller.request.ProductUpdateRequest;
import com.bang.WebBanHang_Project.controller.response.ProductPageResponse;
import com.bang.WebBanHang_Project.controller.response.ProductResponse;

public interface ProductService {

    ProductPageResponse findAll(String keyword, String sort, int page, int size);

    ProductResponse findById(Long id);

    long createProduct(ProductCreationRequest request);

    void updateProduct(ProductUpdateRequest request);

    void deleteProduct(Long id);

}
