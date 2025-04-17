package org.team4.sol_server.domain.portfolio;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<PortfolioEntity, String> {

    @Query(value = "SELECT b.balance AS balance, " +
            "a.user_name AS userName, " +
            "COALESCE(a.personal_investor, 100) AS personalInvestor " +
            "FROM users a, account b " +
            "WHERE a.user_idx = b.user_idx " +
            "AND b.account_type = 'b' " +
            "AND a.user_idx = :userIdx", nativeQuery = true)
    List<Tuple> findUserBalanceNative(@Param("userIdx") Long userIdx);
}
