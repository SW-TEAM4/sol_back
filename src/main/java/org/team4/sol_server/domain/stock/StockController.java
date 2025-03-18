package org.team4.sol_server.domain.stock;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
@Log4j2
public class StockController {

    private final StockService stockService;
    // type에 맞는 데이터 List를 반환
    @GetMapping("/inform")
    @CrossOrigin(origins = "http://localhost:3000") // React 앱만 허용
    public ResponseEntity<List<?>> getStockData(
            @RequestParam String ticker, @RequestParam String type, @RequestParam int page) {
        if ("daily".equals(type)) {
            return ResponseEntity.ok(stockService.getDailyStockData(ticker, page));
        } else if ("weekly".equals(type)) {
            return ResponseEntity.ok(stockService.getWeeklyStockData(ticker, page));
        } else if ("monthly".equals(type)) {
            return ResponseEntity.ok(stockService.getMonthlyStockData(ticker, page));
        } else if ("yearly".equals(type)) {
            return ResponseEntity.ok(stockService.getYearlyStockData(ticker, page));
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonList("Invalid type"));
        }

    }
}
