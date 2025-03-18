package org.team4.sol_server.domain.stock.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockCategoryService {

    @Autowired
    private StockCategoryRepository stockCategoryRepository;

    public List<StockCategoryEntity> getStocksByCategory(String category) {
        // 중복 제거를 위해 Set으로 변환 후 다시 List로 변환
        return stockCategoryRepository.findByCategoryDistinct(category)
                .stream()
                .distinct()
                .toList();
    }
}

