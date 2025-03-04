package com.bang.WebBanHang_Project.service;

import com.bang.WebBanHang_Project.controller.request.*;
import com.bang.WebBanHang_Project.controller.response.ReviewPageResponse;
import com.bang.WebBanHang_Project.controller.response.ReviewResponse;

import java.util.List;

public interface ReviewService {

    ReviewPageResponse getAllReview(String keyword,String sort, int page, int size);

    List<ReviewResponse> getProductReviews(Long id);

    long createReview(ReviewCreationRequest request);

    void updateReview(ReviewUpdateRequest request);

    void deleteReview(ReviewDeleteRequest request);

    void deleteReviewById(Long reviewId);

}
