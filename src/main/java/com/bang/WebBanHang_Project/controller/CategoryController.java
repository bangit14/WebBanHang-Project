package com.bang.WebBanHang_Project.controller;

import com.bang.WebBanHang_Project.controller.request.CategoryCreation;
import com.bang.WebBanHang_Project.controller.request.CategoryUpdate;
import com.bang.WebBanHang_Project.controller.response.ApiResponse;
import com.bang.WebBanHang_Project.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@Tag(name = "Category-Service")
@Slf4j(topic = "CATEGORY-CONTROLLER")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Get Category By Id", description = "API get category from to database")
    @GetMapping("/{categoryId}")
    public ApiResponse getCategoryById(@PathVariable Long categoryId){
        log.info("Get category by id: {}", categoryId);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("category")
                .data(categoryService.findById(categoryId)).build();
    }

    @Operation(summary = "Get Product by categoryId", description = "API get product by categoryId from to database")
    @GetMapping("/product/{categoryId}")
    public ApiResponse getProductByCategoryId(@PathVariable Long categoryId){
        log.info("Get product by categoryId: {}", categoryId);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("product by categoryId")
                .data(categoryService.findByCategoryId(categoryId)).build();
    }

    @Operation(summary = "Create Category", description = "API add new category to database")
    @PostMapping("/create")
    public ApiResponse createCategory(@RequestBody CategoryCreation request){
        log.info("Create category : {}", request);

        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Category created successfully")
                .data(categoryService.create(request)).build();
    }

    @Operation(summary = "Update Category", description = "API update category")
    @PutMapping("/update")
    public ApiResponse updateCategory(@RequestBody CategoryUpdate request){
        log.info("Update category: {}", request);

        categoryService.update(request);

        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("Category updated successfully")
                .data("").build();
    }

    @Operation(summary = "Delete Category", description = "API delete category")
    @DeleteMapping("/del/{categoryId}")
    public ApiResponse deleteCategory(@PathVariable Long categoryId){
        log.info("Delete category: {}", categoryId);

        categoryService.delete(categoryId);

        return ApiResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Category deleted successfully")
                .data("").build();
    }
}
