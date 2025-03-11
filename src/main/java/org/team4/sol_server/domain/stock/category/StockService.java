package org.team4.sol_server.domain.stock.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public List<StockEntity> getStocksByCategory(String category) {
        // 중복 제거를 위해 Set으로 변환 후 다시 List로 변환
        return stockRepository.findByCategoryDistinct(category)
                .stream()
                .distinct()
                .toList();
    }
}

