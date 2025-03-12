package org.team4.sol_server.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team4.sol_server.domain.account.entity.AccountHistoryEntity;
import org.team4.sol_server.domain.account.repository.AccountHistoryRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountHistoryService {
    private final AccountHistoryRepository accountHistoryRepository;

    public List<Map<String, Object>> getTransactionHistory(String accountNo) {
        List<AccountHistoryEntity> historyList = accountHistoryRepository.findByAccountAccountNumberOrderByCreatedDesc(accountNo);

        return historyList.stream().map(tx -> {
            Map<String, Object> transaction = new HashMap<>();
            transaction.put("created", tx.getCreated().toString()); // 날짜/시간 분리
            transaction.put("display_name", tx.getDisplayName()); // 프론트에서 사용할 값
            transaction.put("transfer_balance", tx.getTransferBalance());
            transaction.put("pre_balance", tx.getPreBalance());
            transaction.put("des_wit_type", tx.getDesWitType()); // 입출금 타입 추가
            return transaction;
        }).collect(Collectors.toList());
    }
}
