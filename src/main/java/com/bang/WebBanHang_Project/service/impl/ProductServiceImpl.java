package com.bang.WebBanHang_Project.service.impl;

import com.bang.WebBanHang_Project.controller.request.ProductCreationRequest;
import com.bang.WebBanHang_Project.controller.request.ProductUpdateRequest;
import com.bang.WebBanHang_Project.controller.response.ProductPageResponse;
import com.bang.WebBanHang_Project.controller.response.ProductResponse;
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
    public ProductPageResponse findAll(String keyword, String sort, int page, int size) {
        log.info("findAll start");



        return null;
    }

    @Override
    public ProductResponse findById(Long id) {
        log.info("Find product by id: {}", id);

        ProductEntity productEntity = getProductEntity(id);

        return ProductResponse.builder()
                .id(id)
                .name(productEntity.getName())
                .category(productEntity.getName())
                .units(productEntity.getUnits()).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long createProduct(ProductCreationRequest request) {
        log.info("Create Product: {}", request);

        ProductEntity productByName = productRepository.findByName(request.getName());
        if(productByName != null){
            throw new InvalidDataException("Product already exists");
        }

        ProductEntity product = new ProductEntity();
        product.setName(request.getName());

        Category categoryByName = categoryRepository.findByName(request.getCategory().getName());
        if(categoryByName != null){
            product.setCategory(categoryByName);
            log.info("Set productCategory: {}", categoryByName);
        } else {
            Category newCategory = new Category();
            newCategory.setName(request.getCategory().getName());
            newCategory.setDescription(request.getCategory().getDescription());
            categoryRepository.save(newCategory);
            log.info("Saved category: {}", newCategory);
            product.setCategory(newCategory);
            log.info("Set newProductCategory: {}", newCategory);
        }

//        productRepository.save(product);
//        log.info("Saved product : {}", product);

        List<Unit> unitList = new ArrayList<>();
        request.getUnits().forEach(unit -> {
            Unit unitEntity = new Unit();
            unitEntity.setName(unit.getName());
            unitEntity.setSymbol(unit.getSymbol());
            if(unitRepository.findByName(unit.getName()) == null){
                unitList.add(unitEntity);
            }
        });
        if(!unitList.isEmpty()){
            unitRepository.saveAll(unitList);
            product.setUnits(unitList);
        }

        productRepository.save(product);
        log.info("Saved product : {}", product);

        return product.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProduct(ProductUpdateRequest request) {
        log.info("Updating product: {}", request);

        if(getProductEntity(request.getId()) == null){
            throw new InvalidDataException("Product not found");
        }

        ProductEntity product = getProductEntity(request.getId());
        product.setName(request.getName());

        Category categoryByName = categoryRepository.findByName(request.getCategory().getName());
        if(categoryByName != null){
            product.setCategory(categoryByName);
        } else {
            Category newCategory = new Category();
            newCategory.setName(request.getCategory().getName());
            newCategory.setDescription(request.getCategory().getDescription());
            categoryRepository.save(newCategory);
            log.info("Saved newCategory: {}", newCategory);
            product.setCategory(newCategory);
            log.info("Set productCategory: {}", product);
        }

        productRepository.save(product);
        log.info("Updated product: {}", product);

        List<Unit> unitList = new ArrayList<>();
        request.getUnits().forEach(unit -> {
            Unit unitEntity = new Unit();
            unitEntity.setName(unit.getName());
            unitEntity.setSymbol(unit.getSymbol());
            if(unitRepository.findByName(unit.getName()) == null){
                unitList.add(unitEntity);
            }
        });

        unitRepository.saveAll(unitList);
        log.info("Updated unit: {}", unitList);
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
