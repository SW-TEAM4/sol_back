package org.team4.sol_server.domain.login.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.team4.sol_server.config.BaseException;
import org.team4.sol_server.config.BaseResponseStatus;
import org.team4.sol_server.config.jwt.JwtTokenProvider;
import org.team4.sol_server.domain.login.entity.User;
import org.team4.sol_server.domain.login.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.team4.sol_server.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * JWT를 기반으로 사용자 정보 조회
     */
    public Map<String, Object> getUserInfoFromToken(HttpServletRequest request) throws BaseException {

        String token = jwtTokenProvider.resolveToken(request);
        if (token == null || token.isBlank()) {
            throw new BaseException(EMPTY_JWT);
        }

        if (!jwtTokenProvider.validateToken(token)) {
            throw new BaseException(INVALID_JWT);
        }

        Integer userIdx = jwtTokenProvider.getUserIdx(token);
        if (userIdx == null) {
            throw new BaseException(INVALID_JWT);
        }

        Optional<User> userOptional = userRepository.findByUserIdx(userIdx);
        if (userOptional.isEmpty()) {
            throw new BaseException(BaseResponseStatus.INVALID_USER_JWT);
        }

        User user = userOptional.get();

        return Map.of(
                "userIdx", user.getUserIdx(),
                "username", user.getUsername(),
                "email", user.getEmail()
        );
    }

    /**
     *사용자 정보를 이용하여 JWT 검증 및 반환
     */
    public Map<String, String> getJwtByUserInfo(String authorizationHeader, String email) throws BaseException {
        //JWT 토큰 가져오기 (Bearer 제거)
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new BaseException(SUCCESS);
        }
        String token = authorizationHeader.substring(7);

        //JWT에서 userIdx 가져오기
        int tokenUserIdx = jwtTokenProvider.getUserIdx(token);

        //이메일 기준으로 사용자 찾기
        Optional<User> existingUserOptional = userRepository.findByEmail(email);
        if (existingUserOptional.isEmpty()) {
            throw new BaseException(USER_NOT_FOUND);
        }

        //사용자가 존재하면 기존 JWT 반환
        User user = existingUserOptional.get();

        //토큰에서 추출한 userIdx와 요청한 이메일의 userIdx 비교
        if (user.getUserIdx() != tokenUserIdx) {
            throw new BaseException(UNAUTHORIZED_USER);
        }

        String jwtToken = user.getJwtToken();

        //로그 추가 (콘솔에서 JWT 확인)
        System.out.println("Found JWT for user " + email + ": " + jwtToken);

        //반환 값 수정 (JWT가 `result` 필드에 포함되도록)
        Map<String, String> result = new HashMap<>();
        result.put("token", jwtToken);

        return result;
    }

//    @Transactional
//    public BaseResponseStatus updateUserInfo(int userIdx, String gender, Integer age, Integer job) throws BaseException {
//        User user = userRepository.findById(userIdx)
//                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
//
//        boolean isUpdated = user.updateUserFirstTime(gender, age, job);
//        if (isUpdated) {
//            userRepository.save(user);
//            return BaseResponseStatus.SUCCESS;
//        } else {
//            return BaseResponseStatus.UPDATED_NOT_SUCCESS;
//        }
//    }
}