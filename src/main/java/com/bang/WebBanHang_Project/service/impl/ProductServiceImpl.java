package com.bang.WebBanHang_Project.service.impl;


import com.bang.WebBanHang_Project.controller.request.ProductCreationRequest;
import com.bang.WebBanHang_Project.controller.request.ProductUpdateRequest;
import com.bang.WebBanHang_Project.controller.response.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        Sort.Order order = new Sort.Order(Sort.Direction.ASC,"id");
        if(StringUtils.hasLength(sort)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()){
                String columnName = matcher.group(1);
                if (matcher.group(3).equalsIgnoreCase("asc")){
                    order = new Sort.Order(Sort.Direction.ASC,columnName);
                } else {
                    order = new Sort.Order(Sort.Direction.DESC,columnName);
                }
            }
        }

        int pageNo = 0;
        if(page > 0){
            pageNo = page - 1;
        }

        Pageable pageable = PageRequest.of(pageNo,size,Sort.by(order));
        Page<ProductEntity> entityPage;

        if (StringUtils.hasLength(keyword)){
            keyword = "%" + keyword.toLowerCase() + "%";
            entityPage = productRepository.searchByKeyword(keyword,pageable);
        } else {
            entityPage = productRepository.findAll(pageable);
        }

        return getProductPageResponse(page,size,entityPage);
    }

    @Override
    public ProductResponse findById(Long id) {
        log.info("Find product by id: {}", id);

        ProductEntity productEntity = getProductEntity(id);

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setName(productEntity.getCategory().getName());
        categoryResponse.setDescription(productEntity.getCategory().getDescription());

        List<UnitResponse> unitResponses = new ArrayList<>();
        productEntity.getUnits().forEach(unit -> {
            UnitResponse unitResponse = new UnitResponse();
            unitResponse.setName(unit.getName());
            unitResponse.setSymbol(unit.getSymbol());
            unitResponses.add(unitResponse);
        });

        return ProductResponse.builder()
                .id(id)
                .name(productEntity.getName())
                .sku(productEntity.getSku())
                .category(categoryResponse)
                .units(unitResponses).build();
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
        product.setSku(request.getSku());
        product.setPrice(request.getPrice());

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
        product.setSku(request.getSku());
        product.setPrice(request.getPrice());

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

    private static ProductPageResponse getProductPageResponse(int page, int size, Page<ProductEntity> productEntities){
        log.info("Convert Product Entity Page");

        List<ProductResponse> productList = productEntities.stream().map(entity -> ProductResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .sku(entity.getSku())
                .price(entity.getPrice())
                .category(getCategoryResponse(entity.getCategory()))
                .units(getUnitResponseList(entity.getUnits()))
                .build()
        ).toList();

        ProductPageResponse response = new ProductPageResponse();
        response.setPageNumber(page);
        response.setPageSize(size);
        response.setTotalElements(productEntities.getTotalElements());
        response.setTotalPages(productEntities.getTotalPages());
        response.setProductList(productList);

        return response;
    }

    private static CategoryResponse getCategoryResponse(Category category){
        log.info("Convert category Entity");

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setName(category.getName());
        categoryResponse.setDescription(category.getDescription());
        return categoryResponse;
    }

    private static List<UnitResponse> getUnitResponseList(List<Unit> units){
        log.info("Convert Unit Entity List");

        return units.stream().map(unit -> UnitResponse.builder()
                .name(unit.getName())
                .symbol(unit.getSymbol())
                .build()).toList();
    }
}
