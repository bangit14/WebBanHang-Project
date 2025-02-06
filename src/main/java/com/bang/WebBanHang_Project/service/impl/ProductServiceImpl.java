package com.bang.WebBanHang_Project.service.impl;

import com.bang.WebBanHang_Project.controller.request.ProductCreationRequest;
import com.bang.WebBanHang_Project.controller.request.ProductUpdateRequest;
import com.bang.WebBanHang_Project.exception.InvalidDataException;
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
import java.util.Optional;

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

        if(productRepository.existsById(request.getId())){
            throw new InvalidDataException("Product already exists");
        }

        ProductEntity product = new ProductEntity();
        product.setId(request.getId());
        product.setName(request.getName());

        Category newCategory = new Category();
        newCategory.setName(request.getCategory().getName());
        newCategory.setDescription(request.getCategory().getDescription());

        if(!categoryRepository.existsById(request.getCategory().getId())){
            categoryRepository.save(newCategory);
            log.info("Saved category: {}", newCategory);
        }

        product.setCategory(newCategory);
        log.info("Saved product: {}", product);

        if(product.getId() != null){
            List<Unit> unitList = new ArrayList<>();
            request.getUnits().forEach(unit -> {
                Unit unitEntity = new Unit();
                unitEntity.setId(unit.getId());
                unitEntity.setName(unit.getName());
                unitEntity.setSymbol(unit.getSymbol());
                if(!unitRepository.existsById(unit.getId())){
                    unitList.add(unitEntity);
                }
            });

            if(!unitList.isEmpty()){
                unitRepository.saveAll(unitList);
            }

            product.setUnits(unitList);
//            productRepository.save(product);
//            log.info("Saved product: {}", product);
        }

        return product.getId();
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
