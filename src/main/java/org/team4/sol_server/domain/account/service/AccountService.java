package org.team4.sol_server.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team4.sol_server.domain.account.entity.AccountEntity;
import org.team4.sol_server.domain.account.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Optional<AccountEntity> getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @Transactional
    public boolean transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        Optional<AccountEntity> fromAccountOpt = accountRepository.findByAccountNumber(fromAccountNumber);
        Optional<AccountEntity> toAccountOpt = accountRepository.findByAccountNumber(toAccountNumber);

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
}
