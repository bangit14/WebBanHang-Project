package com.bang.WebBanHang_Project.controller.request;

import com.bang.WebBanHang_Project.common.AttributeType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CategoryAttributeUpdate {

    @NotNull(message = "Attribute ID is required")
    private Long attributeId;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Type is required")
    private String type;

    private boolean required;

    private String options;
}
