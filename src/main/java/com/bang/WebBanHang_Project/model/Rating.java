package com.bang.WebBanHang_Project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_rating")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rating extends AbstractEntity<Long>{

    @Column(name = "productId")
    private Long productId;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "ratings")
    private int ratings;
}
