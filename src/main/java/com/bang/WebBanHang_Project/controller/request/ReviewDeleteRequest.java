package com.bang.WebBanHang_Project.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDeleteRequest {
    private Long productId;
    private Long userId;
}
