package org.team4.sol_server.domain.portfolio;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.team4.sol_server.domain.account.repository.AccountRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;


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
    /* 계좌 잔액 + 투자자성향컬럼*/
    public UserBalanceDTO  getAccountInformation(Long userIdx) {

        System.out.println("userIdx: " + userIdx);

        List<Tuple> result = portfolioRepository.findUserBalanceNative(userIdx);

        Tuple tuple = result.get(0); // 첫 번째 결과를 가져옴
        System.out.println("Tuple Data : " + tuple);
        Long balance = (Long) tuple.get("balance");
        String personalInvestor = tuple.get("personalInvestor", String.class);
        String userName = tuple.get("userName", String.class);

        UserBalanceDTO userBalanceDTO = new UserBalanceDTO(balance, userName, personalInvestor);

        return userBalanceDTO;
    }
}

