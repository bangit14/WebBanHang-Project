package com.bang.WebBanHang_Project.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreationRequest {
    private Long productId;
    private Long userId;
    private String content;
}
