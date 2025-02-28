package com.bang.WebBanHang_Project.service.impl;

import com.bang.WebBanHang_Project.controller.request.RatingRequest;
import com.bang.WebBanHang_Project.controller.request.RatingUpdateRequest;
import com.bang.WebBanHang_Project.controller.response.RatingResponse;
import com.bang.WebBanHang_Project.exception.InvalidDataException;
import com.bang.WebBanHang_Project.exception.ResourceNotFoundException;
import com.bang.WebBanHang_Project.model.Rating;
import com.bang.WebBanHang_Project.repository.RatingRepository;
import com.bang.WebBanHang_Project.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j(topic = "RATING-SERVICE")
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addRating(RatingRequest request) {
        log.info("Add rating for product");

        Rating rating = ratingRepository.findByUserIdAndProductId(request.getUserId(),request.getProductId());

        if(request.getRating() < 0){
            throw new InvalidDataException("Rating not smaller than 0");
        } else if(request.getRating() > 10){
            throw new InvalidDataException("Rating not larger than 10");
        }

        if(rating == null){
            Rating newRating = new Rating();
            newRating.setProductId(request.getProductId());
            newRating.setUserId(request.getUserId());
            newRating.setRatings(request.getRating());
            newRating.setUpdatedAt(LocalDateTime.now());
            ratingRepository.save(newRating);
            return newRating.getId();
        } else {
            throw new ResourceNotFoundException("Rating has already exist");
        }
    }

    @Override
    public void updateRating(Long ratingId,RatingUpdateRequest request) {
        log.info("Update rating for product");

        Rating rating = ratingRepository.findById(ratingId).orElseThrow(
                () -> new ResourceNotFoundException("Rating not found"));

        rating.setRatings(request.getRating());
        rating.setUpdatedAt(LocalDateTime.now());
        ratingRepository.save(rating);

    }

    @Override
    public RatingResponse getAverageRatingByProductId(Long productId) {
        log.info("Get averageRating");

        double ratingPoint = 0.0;

        List<Rating> ratingList = ratingRepository.findByProductId(productId);

        if(ratingList.isEmpty()){
            ratingPoint = 0.0;
        } else {
            ratingPoint = ratingList.stream().mapToInt(Rating::getRatings).sum();
            ratingPoint /= ratingList.size();
        }

        return RatingResponse.builder()
                .productId(productId)
                .rating(ratingPoint)
                .build();
    }
}
