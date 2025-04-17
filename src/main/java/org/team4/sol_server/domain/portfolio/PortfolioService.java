package org.team4.sol_server.domain.portfolio;


import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.team4.sol_server.domain.account.entity.AccountEntity;
import org.team4.sol_server.domain.account.repository.AccountRepository;
import org.team4.sol_server.domain.login.entity.User;
import org.team4.sol_server.domain.login.repository.UserRepository;

import java.util.*;


@Service
@RequiredArgsConstructor
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    public List<PortfolioEntity> getAllPortfolios() {
        return portfolioRepository.findAll();
    }
    /* 계좌 잔액 + 투자자성향컬럼*/
    public UserBalanceDTO  getAccountInformation(int userIdx) throws Exception {

        System.out.println("userIdx: " + userIdx);

        Optional<AccountEntity> account =  accountRepository.findAccountEntityByUserIdx(userIdx);
        Optional<User> user = userRepository.findByUserIdx(userIdx);

        if (!account.isEmpty() && !user.isEmpty()) {
            int personal = Optional.ofNullable(user.get().getPersonalInvestor()).orElse(100);
            return UserBalanceDTO.builder()
                    .balance(account.get().getBalance())
                    .userName(user.get().getUsername())
                    .personalInvestor(personal)
                    .build();
        } else {
            throw new Exception("no user");
        }
    }
}

