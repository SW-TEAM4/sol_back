package org.team4.sol_server.domain.portfolio;


import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.team4.sol_server.domain.account.repository.AccountRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    public List<PortfolioEntity> getAllPortfolios() {
        return portfolioRepository.findAll();
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

