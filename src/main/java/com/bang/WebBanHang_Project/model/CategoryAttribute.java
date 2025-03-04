package com.bang.WebBanHang_Project.model;

import com.bang.WebBanHang_Project.common.AttributeType;
import com.bang.WebBanHang_Project.exception.InvalidDataException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_category_attribute")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryAttribute extends AbstractEntity<Long>{

    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",insertable = false,updatable = false)
    private Category category;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type",nullable = false)
    private AttributeType type;

    @Column(name = "required", nullable = false)
    private boolean required;

    @Column(name = "options")
    private String options;

    public boolean isValid() {

        List<String> errors = new ArrayList<>();

        if(name == null || name.trim().isEmpty()){
            errors.add("Attribute name cannot be empty");
        }

        if(description == null || description.trim().isEmpty()){
            errors.add("Attribute description cannot be empty");
        }

        if(type == null){
            errors.add("Attribute type is required");
        }

        if (options == null || options.isEmpty()){
            errors.add("Options are required for SELECT type attribute");
        }

        if (!errors.isEmpty()){
            throw new InvalidDataException("Invalid attribute data: " + String.join(", ", errors));
        }

        return true;
    }
}
