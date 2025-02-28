package com.bang.WebBanHang_Project.controller.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private UserResponse user;
    private ProductResponse product;
    private String content;
}
