package org.team4.sol_server.domain.portfolio;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class PortfolioService {
    public Map<String, Object> getPortfolioSummary() {
        String apiUrl = "http://localhost:8000/portfolio/list";
        RestTemplate restTemplate = new RestTemplate();

        try {
            PortfolioEntity[] portfolioData = restTemplate.getForObject(apiUrl, PortfolioEntity[].class);

            // 디버깅
            System.out.println("FastAPI에서 받은 데이터: " + Arrays.toString(portfolioData));

            double totalCashBalance = 0;
            double totalPurchaseAmount = 0;
            double totalCurrentValue = 0;
            double totalProfitLoss = 0;

            for (PortfolioEntity entity : portfolioData) {
                totalCashBalance += entity.getKrwBalance() != null ? entity.getKrwBalance() : 0;
                totalPurchaseAmount += entity.getPurchaseAmount() != null ? entity.getPurchaseAmount() : 0;
                totalCurrentValue += entity.getEvaluationAmount() != null ? entity.getEvaluationAmount() : 0;
                totalProfitLoss += entity.getProfitLoss() != null ? entity.getProfitLoss() : 0;
            }

            Map<String, Object> response = new HashMap<>();
            response.put("totalCashBalance", totalCashBalance);
            response.put("totalPurchaseAmount", totalPurchaseAmount);
            response.put("totalCurrentValue", totalCurrentValue);
            response.put("totalProfitLoss", totalProfitLoss);
            response.put("totalProfitLossRate", totalPurchaseAmount > 0 ? (totalProfitLoss / totalPurchaseAmount) * 100 : 0);
            response.put("portfolioList", portfolioData);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error calling FastAPI: " + e.getMessage());
        }
    }
}

