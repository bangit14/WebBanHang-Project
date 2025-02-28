package com.bang.WebBanHang_Project.repository;

import com.bang.WebBanHang_Project.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating,Long> {

    Rating findByUserIdAndProductId(Long userId,Long productId);

    List<Rating> findByProductId(Long productId);
}
