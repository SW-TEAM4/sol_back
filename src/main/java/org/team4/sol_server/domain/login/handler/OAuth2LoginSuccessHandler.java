package org.team4.sol_server.domain.login.handler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.team4.sol_server.config.jwt.JwtTokenProvider;
import org.team4.sol_server.domain.login.entity.CustomOAuth2User;
import org.team4.sol_server.domain.login.repository.UserRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        //현재 로그인한 사용자 정보 가져오기
        CustomOAuth2User customUser = (CustomOAuth2User) authentication.getPrincipal();
        int userIdx = customUser.getUserIdx();
        String jwtToken = customUser.getJwtToken();

        System.out.println("로그인한 userIdx: " + userIdx);
        System.out.println("생성된 JWT: " + jwtToken);

        // JWT를 HttpOnly 쿠키로 저장
        Cookie jwtCookie = new Cookie("jwtToken", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60 * 24);

        // userIdx도 쿠키에 저장 (HttpOnly 해제)
        Cookie userIdxCookie = new Cookie("userIdx", String.valueOf(userIdx));
        userIdxCookie.setHttpOnly(false);  //클라이언트에서 읽을 수 있도록 false 설정
        userIdxCookie.setSecure(false);
        userIdxCookie.setPath("/");
        userIdxCookie.setMaxAge(60 * 60 * 24);

        response.addCookie(jwtCookie);
        response.addCookie(userIdxCookie);

        response.sendRedirect("http://localhost:3000/home");

    }

}