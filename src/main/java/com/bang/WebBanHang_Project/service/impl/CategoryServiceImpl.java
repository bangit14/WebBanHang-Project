package com.bang.WebBanHang_Project.service.impl;

import com.bang.WebBanHang_Project.controller.request.CategoryCreation;
import com.bang.WebBanHang_Project.controller.request.CategoryUpdate;
import com.bang.WebBanHang_Project.controller.response.CategoryResponse;
import com.bang.WebBanHang_Project.controller.response.ProductResponse;
import com.bang.WebBanHang_Project.exception.InvalidDataException;
import com.bang.WebBanHang_Project.exception.ResourceNotFoundException;
import com.bang.WebBanHang_Project.model.Category;
import com.bang.WebBanHang_Project.model.ProductEntity;
import com.bang.WebBanHang_Project.repository.CategoryRepository;
import com.bang.WebBanHang_Project.repository.ProductRepository;
import com.bang.WebBanHang_Project.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j(topic = "CATEGORY-SERVICE")
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public CategoryResponse findById(Long id) {
        log.info("Find category by id: {}", id);

        Category category = getCategory(id);

        return CategoryResponse.builder()
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    @Override
    public List<ProductResponse> findByCategoryId(Long categoryId) {
        log.info("Get product by category id: {}", categoryId);

        List<ProductEntity> productEntities = productRepository.findByCategoryId(categoryId);
        return productEntities.stream().map(enity -> ProductResponse.builder()
                .id(enity.getId())
                .name(enity.getName())
                .price(enity.getPrice())
                .category(getCategoryResponse(enity.getCategory()))
                .build()).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long create(CategoryCreation request) {
        log.info("Create Category: {}", request);

        Category category = categoryRepository.findByName(request.getName());
        if(category != null){
            throw new InvalidDataException("Category already exists");
        }

        Category categoryEntity = new Category();
        categoryEntity.setName(request.getName());
        categoryEntity.setDescription(request.getDescription());
        categoryRepository.save(categoryEntity);
        log.info("Saved category : {}", categoryEntity);

        return categoryEntity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CategoryUpdate request) {
        log.info("Update Category: {}", request);

        Category category = getCategory(request.getId());
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        categoryRepository.save(category);
        log.info("Updated category: {}", category);
    }

    @Override
    public void delete(long id) {
        log.info("Deleting Category: {}", id);

        categoryRepository.deleteById(id);
        log.info("Deleted category id: {}",id);
    }

    private Category getCategory(Long id){
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("category not found"));
    }

    private static CategoryResponse getCategoryResponse(Category category){
        log.info("Convert category Entity");

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setName(category.getName());
        categoryResponse.setDescription(category.getDescription());
        return categoryResponse;
    }
}
