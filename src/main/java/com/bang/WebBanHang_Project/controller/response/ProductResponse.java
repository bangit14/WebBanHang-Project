package com.bang.WebBanHang_Project.controller.response;

import com.bang.WebBanHang_Project.model.Unit;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse implements Serializable {

    private Long id;
    private String name;
    private String category;
    private List<Unit> units;
}
