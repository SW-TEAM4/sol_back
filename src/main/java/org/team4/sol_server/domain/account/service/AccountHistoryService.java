package org.team4.sol_server.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team4.sol_server.domain.account.entity.AccountHistoryEntity;
import org.team4.sol_server.domain.account.repository.AccountHistoryRepository;
import org.team4.sol_server.domain.account.repository.AccountRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountHistoryService {
    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;

    public List<Map<String, Object>> getTransactionHistory(String accountNo) {
        List<AccountHistoryEntity> historyList = accountHistoryRepository.findByAccountAccountNumberOrderByCreatedDesc(accountNo);

        return historyList.stream().map(tx -> {
            Map<String, Object> transaction = new HashMap<>();
            transaction.put("created", tx.getCreated().toString());

            // 계좌번호가 저장된 경우 해당 계좌 주인의 이름을 가져와서 표시
            String displayName = tx.getDisplayName();
            if (displayName.matches("\\d+")) { // 계좌번호 형식이면
                displayName = accountRepository.findUserNameByAccountNumber(displayName);
            }
            transaction.put("display_name", displayName);

            transaction.put("transfer_balance", tx.getTransferBalance());
            transaction.put("pre_balance", tx.getPreBalance());
            transaction.put("des_wit_type", tx.getDesWitType());
            return transaction;
        }).collect(Collectors.toList());
    }

}
