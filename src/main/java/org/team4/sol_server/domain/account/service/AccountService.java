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

            int interest = account.getInterest();
            if (interest <= 0) {
                throw new RuntimeException("적립된 이자가 없습니다."); // 예외 처리 추가
            }

            long previousBalance = account.getBalance();
            long newBalance = previousBalance + interest;

            // 계좌 잔액 업데이트
            account.setInterest(0); // 이자 초기화
            account.setBalance(newBalance);
            accountRepository.save(account);

            // 이자 입금 내역을 거래 기록(AccountHistoryEntity)에 추가
            AccountHistoryEntity transaction = AccountHistoryEntity.builder()
                    .account(account)
                    .preBalance(previousBalance)  // 기존 잔액
                    .transferBalance(interest)   // 이자 금액
                    .desWitType("0")             // '입금' 표시
                    .displayName("이자 입금")     // 거래 설명
                    .build();

            accountHistoryRepository.save(transaction);

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

    @Transactional
    public void addTransaction(String accountNumber, int amount, String desWitType, String displayName) {
        Optional<AccountEntity> accountOpt = accountRepository.findByAccountNumber(accountNumber);

        if (accountOpt.isPresent()) {
            AccountEntity account = accountOpt.get();

            long previousBalance = account.getBalance();
            long newBalance = desWitType.equals("1") ? previousBalance - amount : previousBalance + amount;

            // Account 테이블 balance 업데이트
            account.setBalance(newBalance);
            accountRepository.save(account);

            AccountHistoryEntity transaction = AccountHistoryEntity.builder()
                    .account(account)
                    .preBalance(previousBalance)
                    .transferBalance(amount)
                    .desWitType(desWitType)
                    .displayName(displayName)
                    .build();

            accountHistoryRepository.save(transaction);
            System.out.println("거래 내역 추가 완료 - 새로운 잔액: " + newBalance);
        }
    }
}
