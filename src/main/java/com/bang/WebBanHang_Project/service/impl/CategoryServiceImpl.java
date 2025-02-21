package com.bang.WebBanHang_Project.service.impl;

import com.bang.WebBanHang_Project.common.AttributeType;
import com.bang.WebBanHang_Project.controller.request.CategoryAttributeRequest;
import com.bang.WebBanHang_Project.controller.request.CategoryAttributeUpdate;
import com.bang.WebBanHang_Project.controller.request.CategoryCreation;
import com.bang.WebBanHang_Project.controller.request.CategoryUpdate;
import com.bang.WebBanHang_Project.controller.response.CategoryAttributeResponse;
import com.bang.WebBanHang_Project.controller.response.CategoryResponse;
import com.bang.WebBanHang_Project.controller.response.ProductResponse;
import com.bang.WebBanHang_Project.exception.DuplicateResourceException;
import com.bang.WebBanHang_Project.exception.InvalidDataException;
import com.bang.WebBanHang_Project.exception.ResourceNotFoundException;
import com.bang.WebBanHang_Project.model.Category;
import com.bang.WebBanHang_Project.model.CategoryAttribute;
import com.bang.WebBanHang_Project.model.ProductEntity;
import com.bang.WebBanHang_Project.repository.CategoryAttributeRepository;
import com.bang.WebBanHang_Project.repository.CategoryRepository;
import com.bang.WebBanHang_Project.repository.ProductRepository;
import com.bang.WebBanHang_Project.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j(topic = "CATEGORY-SERVICE")
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryAttributeRepository categoryAttributeRepository;

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
    public Page<CategoryResponse> findCategoryCustom(String keyword, String sort, int page, int size) {
        return null;
    }

    @Override
    public Page<CategoryAttributeResponse> getCategoryAttribute(Long categoryId,Pageable pageable) {
        log.info("Get CategoryAttribute");

        if(!categoryRepository.existsById(categoryId)){
            throw new ResourceNotFoundException("Category not found");
        }

        Page<CategoryAttribute> attributes = categoryAttributeRepository.findByCategoryId(categoryId,pageable);

        return attributes.map(categoryAttribute -> {
            CategoryAttributeResponse response = new CategoryAttributeResponse();
            response.setId(categoryAttribute.getId());
            response.setCategoryId(categoryAttribute.getCategoryId());
            response.setName(categoryAttribute.getName());
            response.setDescription(categoryAttribute.getDescription());
            response.setType(categoryAttribute.getType());
            response.setRequired(true);
            response.setOptions(categoryAttribute.getOptions());
            return response;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addAttribute(Long categoryId,CategoryAttributeRequest request) {
        log.info("Add CategoryAttribute");

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (categoryAttributeRepository.existsByCategoryIdAndName(categoryId,request.getName())){
            throw new DuplicateResourceException("Attribute with name '" + request.getName()
                    + "' already exists in category " + categoryId);
        }

        CategoryAttribute attribute = new CategoryAttribute();
        attribute.setCategoryId(categoryId);
        attribute.setName(request.getName());
        attribute.setDescription(request.getDescription());

        String str = request.getType();
        if(str.trim().equalsIgnoreCase("select")){
            attribute.setType(AttributeType.SELECT);
        } else if (str.trim().equalsIgnoreCase("text")){
            attribute.setType(AttributeType.TEXT);
        } else if (str.trim().equalsIgnoreCase("number")){
            attribute.setType(AttributeType.NUMBER);
        } else if(str.trim().equalsIgnoreCase("date")){
            attribute.setType(AttributeType.DATE);
        } else if (str.trim().equalsIgnoreCase("boolean")){
            attribute.setType(AttributeType.BOOLEAN);
        }
        attribute.setRequired(request.isRequired());
        attribute.setOptions(request.getOptions());


       // if(!attribute.isValid()){
            categoryAttributeRepository.save(attribute);
            //return attribute.getId();
       // } else {
        //    log.error("Invalid attribute data");
       // }
        return attribute.getId();
    }

    @Override
    public void updateAttribute(CategoryAttributeUpdate request) {
        log.info("Updated CategoryAttribute");

        CategoryAttribute categoryAttribute = categoryAttributeRepository.findById(request.getAttributeId())
                .orElseThrow(() -> new ResourceNotFoundException("Attribute not found"));

        if (categoryAttributeRepository.existsByCategoryIdAndNameAndIdNot(
                categoryAttribute.getCategoryId(), request.getName(), request.getAttributeId())) {
            throw new DuplicateResourceException("Attribute name already exist in this category");
        }

        categoryAttribute.setId(request.getAttributeId());
        categoryAttribute.setCategoryId(request.getCategoryId());
        categoryAttribute.setName(request.getName());
        categoryAttribute.setDescription(request.getDescription());

        String str = request.getType();
        if(str.trim().equalsIgnoreCase("select")){
            categoryAttribute.setType(AttributeType.SELECT);
        } else if (str.trim().equalsIgnoreCase("text")){
            categoryAttribute.setType(AttributeType.TEXT);
        } else if (str.trim().equalsIgnoreCase("number")){
            categoryAttribute.setType(AttributeType.NUMBER);
        } else if(str.trim().equalsIgnoreCase("date")){
            categoryAttribute.setType(AttributeType.DATE);
        } else if (str.trim().equalsIgnoreCase("boolean")){
            categoryAttribute.setType(AttributeType.BOOLEAN);
        }

        categoryAttribute.setRequired(true);
        categoryAttribute.setOptions(request.getOptions());

        if (categoryAttribute.isValid()) {
            throw new InvalidDataException("Invalid attribute data");
        }

        categoryAttributeRepository.save(categoryAttribute);
        log.info("Updated categoryAttribute");

    }

    @Override
    public void deleteAttribute(long attributeId) {
        log.info("Deleted categoryAttribute");

        if(!categoryAttributeRepository.existsById(attributeId)){
            throw new ResourceNotFoundException("Attribute not found");
        }

        categoryAttributeRepository.deleteById(attributeId);
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

        if(!categoryRepository.existsById(id)){
            throw new ResourceNotFoundException("Category not found");
        }

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
