package org.team4.sol_server.domain.login.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.team4.sol_server.config.jwt.JwtTokenProvider;
import org.team4.sol_server.domain.login.entity.CustomOAuth2User;
import org.team4.sol_server.domain.login.entity.User;
import org.team4.sol_server.domain.login.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String oauthClientName = userRequest.getClientRegistration().getClientName();
        System.out.println("로그인 클라: " + oauthClientName);

        // 사용자 정보 초기화
        String username = null;
        String email = null;

        // OAuth 제공자별 데이터 매핑
        Map<String, Object> attributes = oAuth2User.getAttributes();
        if ("kakao".equalsIgnoreCase(oauthClientName)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            username = (String) profile.get("nickname");
            email = (String) kakaoAccount.get("email");
        } else if ("naver".equalsIgnoreCase(oauthClientName)) {
            Map<String, Object> responseMap = (Map<String, Object>) attributes.get("response");

            username = (String) responseMap.get("name");
            email = (String) responseMap.get("email");
        }

        System.out.println("사용자 정보 확인 - username: " + username + ", email: " + email);

        // 메일 기준으로 사용자 검색
        Optional<User> existingUserOptional = userRepository.findByEmail(email);
        int userIdx;
        String jwtToken;

        if (existingUserOptional.isPresent()) {
            // 기존 사용자: userIdx 유지, JWT 갱신
            User existingUser = existingUserOptional.get();
            userIdx = existingUser.getUserIdx();
            jwtToken = jwtTokenProvider.createJwt(userIdx);
            existingUser.updateJwtToken(jwtToken);
            existingUser.updateTime();
            userRepository.save(existingUser);
        } else {
            // 새로운 사용자 등록
            User newUser = User.builder()
                    .username(username)
                    .email(email)
                    .loginType(oauthClientName)
                    .created(LocalDateTime.now())
                    .updated(LocalDateTime.now())
                    .build();

            // 저장 후 userIdx 가져오기
            User savedUser = userRepository.save(newUser);
            userIdx = savedUser.getUserIdx();
            jwtToken = jwtTokenProvider.createJwt(userIdx);

            // JWT 저장
            savedUser.updateJwtToken(jwtToken);
            userRepository.save(savedUser);
        }

        System.out.println("발급된 JWT: " + jwtToken + ", userIdx: " + userIdx);

        // ✅ userIdx 포함하여 CustomOAuth2User 반환
        return new CustomOAuth2User(userIdx, username, jwtToken, attributes);
    }
}
