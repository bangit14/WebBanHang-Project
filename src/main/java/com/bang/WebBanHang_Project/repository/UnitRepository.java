package com.bang.WebBanHang_Project.repository;

import com.bang.WebBanHang_Project.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit,Long> {
    Unit findBySymbol(String symbol);

    Unit findByName(String name);
}
