package org.team4.sol_server.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.team4.sol_server.domain.account.entity.AccountHistoryEntity;

import java.util.List;

@Repository
public interface AccountHistoryRepository extends JpaRepository<AccountHistoryEntity, Integer> {
    // 특정 계좌의 거래 내역 조회
    List<AccountHistoryEntity> findByAccountAccountNumber(String accountNumber);
}
