package com.bang.WebBanHang_Project.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RatingResponse {
    private Long productId;
    private double rating;
}
