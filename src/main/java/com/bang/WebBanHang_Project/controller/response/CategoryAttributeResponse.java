package com.bang.WebBanHang_Project.controller.response;

import com.bang.WebBanHang_Project.common.AttributeType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryAttributeResponse {

    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private AttributeType type;
    private boolean required;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> options;
}
