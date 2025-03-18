package org.team4.sol_server.domain.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("SELECT MIN(s.date) FROM Stock s WHERE s.ticker = :ticker")
    Optional<LocalDate> findMinDateByTicker(@Param("ticker") String ticker);


    // 티커를 이용해서 ( startDate , endDate ] 의 스톡 데이터 조회
    @Query("SELECT s FROM Stock s WHERE s.ticker = :ticker AND s.date >= :startDate AND s.date < :endDate")
    List<Stock> findByTickerAndDateRangeExclusive(
            @Param("ticker") String ticker, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate
    );
}
