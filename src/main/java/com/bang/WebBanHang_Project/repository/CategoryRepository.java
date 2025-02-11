package com.bang.WebBanHang_Project.repository;

import com.bang.WebBanHang_Project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Category findByName(String name);

}
