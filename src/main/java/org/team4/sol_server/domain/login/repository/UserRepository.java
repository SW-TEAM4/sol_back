package org.team4.sol_server.domain.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team4.sol_server.domain.login.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserIdx(int userIdx);
}
