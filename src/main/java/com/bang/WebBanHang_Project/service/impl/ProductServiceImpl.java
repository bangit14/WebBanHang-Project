package com.bang.WebBanHang_Project.service.impl;

import com.bang.WebBanHang_Project.controller.request.ProductCreationRequest;
import com.bang.WebBanHang_Project.controller.request.ProductUpdateRequest;
import com.bang.WebBanHang_Project.exception.ResourceNotFoundException;
import com.bang.WebBanHang_Project.model.Category;
import com.bang.WebBanHang_Project.model.ProductEntity;
import com.bang.WebBanHang_Project.model.Unit;
import com.bang.WebBanHang_Project.repository.CategoryRepository;
import com.bang.WebBanHang_Project.repository.ProductRepository;
import com.bang.WebBanHang_Project.repository.UnitRepository;
import com.bang.WebBanHang_Project.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j(topic = "PRODUCT-SERVICE")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UnitRepository unitRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long createProduct(ProductCreationRequest request) {
        log.info("Create Product: {}", request);


        Category category = categoryRepository.findById(request.getCategory().getId())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setId(request.getCategory().getId());
                    newCategory.setName(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        log.info("Saved category: {}", request.getCategory());

        List<ProductEntity> productList = new ArrayList<>();
        request.getUnits().forEach(unit -> {
            ProductEntity product = new ProductEntity();
            product.setName(request.getName());
            product.setCategory(request.getCategory());
            product.setUnit(unit.getId());
            productList.add(product);
        });

        log.info("product id: {}", request.getId());
        List<Unit> unitList = new ArrayList<>();
        request.getUnits().forEach(unit -> {
            Unit unitEntity = new Unit();
            unitEntity.setId(unit.getId());
            unitEntity.setName(unit.getName());
            unitEntity.setDescription(unit.getDescription());
            unitList.add(unitEntity);
        });
        unitRepository.saveAll(unitList);
        log.info("Saved units: {}", unitList);

        productRepository.saveAll(productList);
        log.info("Saved product :{}", productList);
        return productList.get(0).getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProduct(ProductUpdateRequest request) {
        log.info("Updating product: {}", request);

        ProductEntity product = getProductEntity(request.getId());
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        Category category = getCategory(request.getId());
        if(category == null){
            categoryRepository.save(request.getCategory());
        }

    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product: {}", id);

        productRepository.deleteById(id);
        log.info("Deleted product id: {}", id);
    }

    private ProductEntity getProductEntity(Long id){
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    private Category getCategory(Long id){
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }
}
