package org.team4.sol_server.domain.account.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team4.sol_server.domain.account.dto.AccountDTO;
import org.team4.sol_server.domain.account.entity.AccountEntity;
import org.team4.sol_server.domain.account.repository.AccountRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    // 데이터베이스에서 계좌 조회, 입금, 출금, 이체, 이체 비율 설정, 이자 계산 등을 처리
    private final AccountRepository accountRepository;

    public Optional<AccountEntity> getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @Transactional
    public boolean transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        Optional<AccountEntity> fromAccountOpt = accountRepository.findByAccountNumber(fromAccountNumber); // 출금 계좌
        Optional<AccountEntity> toAccountOpt = accountRepository.findByAccountNumber(toAccountNumber); // 입금 계좌

        if (fromAccountOpt.isPresent() && toAccountOpt.isPresent()) {
            AccountEntity fromAccount = fromAccountOpt.get();
            AccountEntity toAccount = toAccountOpt.get();

            if (fromAccount.getBalance() >= amount) {
                fromAccount.setBalance(fromAccount.getBalance() - amount);
                toAccount.setBalance(toAccount.getBalance() + amount);
                accountRepository.save(fromAccount);
                accountRepository.save(toAccount);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean deposit(String accountNumber, double amount) {
        Optional<AccountEntity> accountOpt = accountRepository.findByAccountNumber(accountNumber);

        if (accountOpt.isPresent() && amount > 0) {
            AccountEntity account = accountOpt.get();

            account.setBalance(account.getBalance() + amount);  //  입금 로직
            accountRepository.save(account);
            return true;
        }
        return false;
    }

    // 파킹 통장 투자 비율 설정
    @Transactional
    public void setTransferRatio(String accountNumber, int ratio) {
        Optional<AccountEntity> accountOpt = accountRepository.findByAccountNumber(accountNumber);
        accountOpt.ifPresent(account -> {
            account.setInvestorRatio(ratio); // 파킹통장 "투자 비율" 업데이트
            accountRepository.save(account);
        });
    }

    // collectInterest() --> 투자 비율을 기준으로 이자 계산 및 계좌 잔액 증가 함수
    @Transactional
    public AccountDTO collectInterest(String accountNumber) {
        Optional<AccountEntity> accountOpt = accountRepository.findByAccountNumber(accountNumber);
        if (accountOpt.isPresent()) {
            AccountEntity account = accountOpt.get(); // 계좌 정보 가져오기
            // 이자 계산 = 잔액 * (이자율/100.0)
            int interest = (int) (account.getBalance() * (account.getInterestRatio() / 100.0));
            account.setInterest(interest); // 이자 저장
            account.setBalance(account.getBalance() + interest); // 기존 잔액 + 이자
            accountRepository.save(account);

            return AccountDTO.builder()
                    .accountNumber(account.getAccountNumber())
                    .balance(account.getBalance())
                    .build();
        }
        return null;
    }

    // 받을 이자 조회
    public int getInterest(String accountNumber) {
        Optional<AccountEntity> accountOpt = accountRepository.findByAccountNumber(accountNumber);
        return accountOpt.map(AccountEntity::getInterest).orElse(0);
    }
}
