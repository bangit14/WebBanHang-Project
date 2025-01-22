package com.bang.WebBanHang_Project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tbl_unit")
public class Unit extends AbstractEntity<Integer>{

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

}
