package org.team4.sol_server.domain.stock.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockCategoryService {

    @Autowired
    private StockCategoryRepository stockCategoryRepository;

    public List<StockCategoryEntity> getStocksByCategory(String category) {

        return stockCategoryRepository.findByCategory(category);
    }
}

