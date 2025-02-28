package com.bang.WebBanHang_Project.service;

import com.bang.WebBanHang_Project.controller.request.RatingRequest;
import com.bang.WebBanHang_Project.controller.request.RatingUpdateRequest;
import com.bang.WebBanHang_Project.controller.response.RatingResponse;

public interface RatingService {

    long addRating(RatingRequest request);

    void updateRating(Long ratingId,RatingUpdateRequest request);

    RatingResponse getAverageRatingByProductId(Long productId);
}
