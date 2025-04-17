package org.team4.sol_server.domain.stock.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockCategoryRepository extends JpaRepository<StockCategoryEntity, Long> {
    @Query("SELECT s FROM StockCategoryEntity s WHERE s.category = :category")
    List<StockCategoryEntity> findByCategory(@Param("category") String category);
}
