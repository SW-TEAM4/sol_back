package org.team4.sol_server.domain.user.service;

import org.team4.sol_server.domain.user.entity.User;

import java.util.Map;

public interface UserService {
    User savePersonalInvestor(int userIdx, int score);
    int getPersonalInvestor(int userIdx);
    User getUserById(int userIdx);
}
