package com.bang.WebBanHang_Project.controller.response;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse implements Serializable {

    private Long id;
    private String name;
    private String sku;
    private BigDecimal price;
    private CategoryResponse category;
    private List<UnitResponse> units;
}
