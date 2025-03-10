package org.team4.sol_server.domain.stock.category;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/category/{category}")
    public ResponseEntity<List<StockEntity>> getStocksByCategory(@PathVariable String category) {
        List<StockEntity> stocks = stockService.getStocksByCategory(category);
        return ResponseEntity.ok(stocks);
    }
}


