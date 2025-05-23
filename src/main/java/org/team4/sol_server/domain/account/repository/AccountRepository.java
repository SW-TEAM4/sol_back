package org.team4.sol_server.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.team4.sol_server.domain.account.entity.AccountEntity;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    @Query("SELECT a FROM AccountEntity a WHERE a.accountNumber = :accountNumber and a.accountType='a'" )
    Optional<AccountEntity> findByAccountNumber(String accountNumber);

    /* 이름 추출 */
    @Query("SELECT a.user.username FROM AccountEntity a WHERE a.accountNumber = :accountNumber and a.accountType='a'")
    String findUserNameByAccountNumber(String accountNumber);

    /*계좌 추출*/
    @Query("SELECT a.accountNumber FROM AccountEntity a WHERE a.user.userIdx = :userIdx and a.accountType='a'")
    Optional<AccountEntity> findAccountNoByUserIdx(@Param("userIdx")int userIdx);

    @Query("SELECT a FROM AccountEntity a WHERE a.user.userIdx = :userIdx and a.accountType='a'")
    Optional<AccountEntity> findAccountEntityByUserIdx(@Param("userIdx")int userIdx);

    // 특정 사용자의 파킹통장 잔액 조회
    @Query("SELECT a.balance FROM AccountEntity a WHERE a.user.userIdx = :userIdx AND a.accountType = 'a'")
    Optional<Long> findParkingBalanceByUserIdx(@Param("userIdx") int userIdx);
}
