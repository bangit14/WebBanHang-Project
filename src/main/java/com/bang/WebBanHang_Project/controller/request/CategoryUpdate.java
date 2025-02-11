package com.bang.WebBanHang_Project.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryUpdate {
    private Long id;
    private String name;
    private String description;
}
