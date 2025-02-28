package com.bang.WebBanHang_Project.service.impl;

import com.bang.WebBanHang_Project.controller.request.*;
import com.bang.WebBanHang_Project.controller.response.ProductResponse;
import com.bang.WebBanHang_Project.controller.response.RatingResponse;
import com.bang.WebBanHang_Project.controller.response.ReviewResponse;
import com.bang.WebBanHang_Project.controller.response.UserResponse;
import com.bang.WebBanHang_Project.exception.InvalidDataException;
import com.bang.WebBanHang_Project.exception.ResourceNotFoundException;
import com.bang.WebBanHang_Project.model.ProductEntity;
import com.bang.WebBanHang_Project.model.Review;
import com.bang.WebBanHang_Project.model.UserEntity;
import com.bang.WebBanHang_Project.repository.ProductRepository;
import com.bang.WebBanHang_Project.repository.ReviewRepository;
import com.bang.WebBanHang_Project.repository.UserRepository;
import com.bang.WebBanHang_Project.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j(topic = "REVIEW-SERVICE")
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    @Override
    public List<ReviewResponse> getProductReviews(Long productId) {
        log.info("Get review by productId");

        List<Review> reviews = reviewRepository.getListByProductId(productId);

        return reviews.stream().map(
            reviewEntity -> ReviewResponse.builder()
                    .user(getUserResponse(reviewEntity.getUser()))
                    .product(getProductResponse(reviewEntity.getProduct()))
                    .content(reviewEntity.getContent()).build()).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long createReview(ReviewCreationRequest request) {
        log.info("Create new review for product");

        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setContent(request.getContent());
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);

        return review.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReview(ReviewUpdateRequest request) {
        log.info("Update review for product");

        Review review = reviewRepository.findByUserIdAndProductId(request.getUserId(),request.getProductId());

        if(review != null){
            review.setContent(request.getContent());
            review.setUpdatedAt(LocalDateTime.now());
            reviewRepository.save(review);
        } else {
            throw new InvalidDataException("Review not found in repository");
        }
    }

    @Override
    public void deleteReview(ReviewDeleteRequest request) {
        log.info("Delete review");

        Review review = reviewRepository.findByUserIdAndProductId(request.getUserId(),request.getProductId());

        if (review != null){
            reviewRepository.delete(review);
        } else {
            throw new InvalidDataException("Review not found");
        }

    }

    @Override
    public void deleteReviewById(Long reviewId) {
        log.info("Delete review by reviewId");

        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ResourceNotFoundException("Review not found"));

        reviewRepository.delete(review);
    }

    private UserResponse getUserResponse(UserEntity user){
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email("")
                .username("")
                .gender(user.getGender())
                .phone("")
                .birthday(user.getBirthday())
                .build();
    }

    private ProductResponse getProductResponse(ProductEntity product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .price(product.getPrice())
                .build();
    }
}
