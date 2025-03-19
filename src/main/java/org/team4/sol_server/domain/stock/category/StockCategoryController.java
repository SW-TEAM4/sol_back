package org.team4.sol_server.domain.stock.category;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@CrossOrigin(origins = "http://localhost:3000") // React 앱만 허용
public class StockCategoryController {

    @Autowired
    private StockCategoryService stockCategoryService;

    @GetMapping("/category/{category}")
    public ResponseEntity<List<StockCategoryEntity>> getStocksByCategory(@PathVariable String category) {
        List<StockCategoryEntity> stocks = stockCategoryService.getStocksByCategory(category);
        return ResponseEntity.ok(stocks);
    }
}


