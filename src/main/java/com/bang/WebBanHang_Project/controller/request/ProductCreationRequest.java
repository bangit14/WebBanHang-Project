package com.bang.WebBanHang_Project.controller.request;

import com.bang.WebBanHang_Project.model.Category;
import com.bang.WebBanHang_Project.model.Inventory;
import com.bang.WebBanHang_Project.model.Unit;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProductCreationRequest {

    private Long id;
    private String name;
    private CategoryCreation category;
    private List<UnitCreation> units;

}
