package com.bang.WebBanHang_Project.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name = "tbl_inventory")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory extends AbstractEntity<Long>{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "quantity",nullable = false)
    private Long quantity;

    @Column(name = "location")
    private String location;

    @Column(name = "notes")
    private String notes;

    @Column(name = "discount")
    private Long discount;

    @Column(name = "status")
    private String status;
}
