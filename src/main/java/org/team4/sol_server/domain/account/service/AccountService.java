package org.team4.sol_server.domain.account.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team4.sol_server.domain.account.dto.AccountDTO;
import org.team4.sol_server.domain.account.entity.AccountEntity;
import org.team4.sol_server.domain.account.entity.AccountHistoryEntity;
import org.team4.sol_server.domain.account.repository.AccountHistoryRepository;
import org.team4.sol_server.domain.account.repository.AccountRepository;

import java.util.List;
import java.util.Optional;
/*
파일명 : AccountService.java
생성자 : JDeok
날 짜  : 2025.03.05
시 간  : 오후 02:14
기 능  : 계좌Service
Params :
Return :
변경사항
     - 2025.03.05 : JDeok(최초작성)
*/
@Service
@RequiredArgsConstructor
public class AccountService {
    // 데이터베이스에서 계좌 조회, 입금, 출금, 이체, 이체 비율 설정, 이자 계산 등을 처리
    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;

    /** 계좌테이블 조회 **/
    public Optional<AccountEntity> getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    /**
     * 계좌이체 로직
     **/
    @Transactional
    public String transfer(String fromAccountNumber, String toAccountNumber, Long amount) {
        Optional<AccountEntity> fromAccountOpt = accountRepository.findByAccountNumber(fromAccountNumber); // 출금 계좌
        Optional<AccountEntity> toAccountOpt = accountRepository.findByAccountNumber(toAccountNumber);     // 입금 계좌

        AccountEntity fromAccount = fromAccountOpt.get();
        AccountEntity toAccount = toAccountOpt.get();

        if(fromAccount.getBalance() < amount) {
            return "잔액이 부족합니다.";
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return "이체 완료 되었습니다.";
    }

    /** 계좌 체크 **/
    public String checkAccounts(String fromAccount, String toAccount) {
        Optional<AccountEntity> fromAccountOpt = accountRepository.findByAccountNumber(fromAccount);
        Optional<AccountEntity> toAccountOpt = accountRepository.findByAccountNumber(toAccount);

        if(fromAccountOpt.equals(toAccountOpt)) {
            return "출금 계좌와 입금 계좌가 동일할 수 없습니다.";
        }
        if (fromAccountOpt.isEmpty()) {
            return "출금 계좌가 존재하지 않습니다.";
        }
        if (toAccountOpt.isEmpty()) {
            return "입금 계좌가 존재하지 않습니다.";
        }


        return "VALID";  //  모든 계좌가 정상적으로 존재함

    }
    /** 입금 로직 (test용) **/
    @Transactional
    public boolean deposit(String accountNumber, Long amount) {
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
            account.setInvestorRatio(ratio);
            accountRepository.save(account);
        });
    }

    // 파킹 통장 투자 비율 조회
    @Transactional
    public Integer getTransferRatio(String accountNumber) {
        Optional<AccountEntity> accountOpt = accountRepository.findByAccountNumber(accountNumber);
        return accountOpt.map(AccountEntity::getInvestorRatio).orElse(0);
    }

    // collectInterest() --> 투자 비율을 기준으로 이자 계산 및 계좌 잔액 증가 함수
    @Transactional
    public AccountDTO collectInterest(String accountNumber) {
        Optional<AccountEntity> accountOpt = accountRepository.findByAccountNumber(accountNumber);
        if (accountOpt.isPresent()) {
            AccountEntity account = accountOpt.get();
            // 이자 계산 = 잔액 * (이자율/100.0)
            int interest = (int) (account.getBalance() * (account.getInterestRatio() / 100.0));
            account.setInterest(interest);
            account.setBalance(account.getBalance() + interest);
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

    // 거래 내역 조회
    @Transactional(readOnly = true)
    public List<AccountHistoryEntity> getAccountHistory(String accountNumber) {
        return accountHistoryRepository.findByAccountAccountNumber(accountNumber);
    }
}
