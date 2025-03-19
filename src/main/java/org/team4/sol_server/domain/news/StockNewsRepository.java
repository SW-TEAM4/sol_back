package org.team4.sol_server.domain.news;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockNewsRepository extends JpaRepository<StockNewsEntity, Long> {
}