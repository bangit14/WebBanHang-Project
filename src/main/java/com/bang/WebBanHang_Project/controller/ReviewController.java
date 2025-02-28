package com.bang.WebBanHang_Project.controller;


import com.bang.WebBanHang_Project.controller.request.*;
import com.bang.WebBanHang_Project.controller.response.ApiResponse;
import com.bang.WebBanHang_Project.service.RatingService;
import com.bang.WebBanHang_Project.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
@Slf4j(topic = "REVIEW-CONTROLLER")
@Tag(name = "Review-Controller")
@RequiredArgsConstructor
@Validated
public class ReviewController {

    private final ReviewService reviewService;
    private final RatingService ratingService;

    @Operation(summary = "Get Review", description = "API get review by id from to database")
    @GetMapping("/product/{productId}")
    public ApiResponse getReview(@PathVariable Long productId){
        log.info("Get order detail by Id: {}", productId);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get order detail")
                .data(reviewService.getProductReviews(productId)).build();
    }

    @Operation(summary = "Create Review", description = "API create new review to database")
    @PostMapping("/create")
    public ApiResponse createOrder(@RequestBody ReviewCreationRequest request){
        log.info("Create order: {}", request);

        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Review created successfully")
                .data(reviewService.createReview(request)).build();
    }

    @Operation(summary = "Update Review", description = "API update review to database")
    @PutMapping("/upd")
    public ApiResponse updateReview(@RequestBody ReviewUpdateRequest request){
        log.info("Update review");

        reviewService.updateReview(request);

        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("Review updated successfully")
                .data("").build();
    }

    @Operation(summary = "Delete Review", description = "API delete review")
    @DeleteMapping("/del")
    public ApiResponse deleteReview(@RequestBody ReviewDeleteRequest request){
        log.info("Delete review");

        reviewService.deleteReview(request);

        return ApiResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Deleted review successfully")
                .data("").build();
    }

    @Operation(summary = "Delete Review", description = "API delete review")
    @DeleteMapping("/del/{reviewId}")
    public ApiResponse deleteReview(@PathVariable Long reviewId){
        log.info("Delete review by id");

        reviewService.deleteReviewById(reviewId);

        return ApiResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Deleted review successfully")
                .data("").build();
    }

    @Operation(summary = "Add Rating", description = "API add rating")
    @PostMapping("/rating")
    public ApiResponse addRating(@RequestBody RatingRequest request){
        log.info("Add rating");

        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Add rating successfully")
                .data(ratingService.addRating(request)).build();
    }

    @Operation(summary = "Update Rating", description = "API update rating")
    @PutMapping("/rating/{ratingId}")
    public ApiResponse updateRating(@PathVariable Long ratingId, @RequestBody RatingUpdateRequest request){
        log.info("Update rating");

        ratingService.updateRating(ratingId,request);

        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("Update rating successfully")
                .data("").build();
    }

    @Operation(summary = "Get productRating", description = "API get ratingProduct")
    @GetMapping("/ratings/product/{productId}")
    public ApiResponse getProductRating(@PathVariable Long productId){
        log.info("Get productRating");

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Get productRating")
                .data(ratingService.getAverageRatingByProductId(productId)).build();
    }

}
