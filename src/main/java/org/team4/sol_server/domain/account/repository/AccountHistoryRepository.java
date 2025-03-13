package org.team4.sol_server.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.team4.sol_server.domain.account.entity.AccountHistoryEntity;

import java.util.List;

@Repository
public interface AccountHistoryRepository extends JpaRepository<AccountHistoryEntity, Integer> {
    // 기존 메서드 → 정렬 추가
    List<AccountHistoryEntity> findByAccountAccountNumberOrderByCreatedDesc(String accountNumber);
}
