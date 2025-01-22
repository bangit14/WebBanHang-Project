package com.bang.WebBanHang_Project.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_inventory")
@Builder
public class Inventory extends AbstractEntity<Long>{

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "product_id")
    private ProductEntity product;

}
