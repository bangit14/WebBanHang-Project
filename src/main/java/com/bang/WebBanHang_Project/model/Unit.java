package com.bang.WebBanHang_Project.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tbl_unit")
public class Unit extends AbstractEntity<Long>{

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "symbol")
    private String symbol;

    @ManyToMany(mappedBy = "units")
    private List<ProductEntity> products;

}
