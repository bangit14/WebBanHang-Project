package com.bang.WebBanHang_Project.controller.response;

import com.bang.WebBanHang_Project.common.AttributeType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryAttributeResponse {

    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private AttributeType type;
    private boolean required;
    private String options;
}
