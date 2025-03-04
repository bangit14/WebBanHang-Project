package com.bang.WebBanHang_Project.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class ReviewPageResponse extends PageResponseAbstract implements Serializable {
    List<ReviewResponse> reviews;
}
