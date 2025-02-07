package com.bang.WebBanHang_Project.controller.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductPageResponse extends PageResponseAbstract{
    List<ProductResponse> productList;
}
