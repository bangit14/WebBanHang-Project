package com.bang.WebBanHang_Project.controller.request;

import com.bang.WebBanHang_Project.model.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreation extends AbstractEntity<Long> {

    private String name;
    private String description;
}
