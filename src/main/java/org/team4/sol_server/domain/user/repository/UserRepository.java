package org.team4.sol_server.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team4.sol_server.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserEmail(String email);
}
