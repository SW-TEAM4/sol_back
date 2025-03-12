package org.team4.sol_server.domain.stock.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {
    @Query("SELECT DISTINCT s FROM StockEntity s WHERE s.category = :category")
    List<StockEntity> findByCategoryDistinct(@Param("category") String category);
}
