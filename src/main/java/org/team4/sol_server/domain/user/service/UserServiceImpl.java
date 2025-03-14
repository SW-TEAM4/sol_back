package org.team4.sol_server.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team4.sol_server.domain.user.entity.User;
import org.team4.sol_server.domain.user.repository.UserRepository;

/*
파일명 : UserServiceImpl.java
생성자 : JM
날 짜  : 2025.03.01
시 간  : 오후 03:00
기 능  : 유저 정보 서비스
Params :
Return :
변경사항
     - 2025.03.01 : JM(최초작성)
*/

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User savePersonalInvestor(int userIdx, int score) {
        User user = userRepository.findById(userIdx)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPersonalInvestor(score);
        return userRepository.save(user);
    }

    @Override
    public int getPersonalInvestor(int userIdx) {
        User user = userRepository.findById(userIdx)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getPersonalInvestor();
    }

    @Override
    public User getUserById(int userIdx) {
        return userRepository.findById(userIdx).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
