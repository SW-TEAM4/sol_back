package org.team4.sol_server.config.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.team4.sol_server.domain.login.entity.User;
import org.team4.sol_server.domain.login.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserRepository userRepository;

    private final long tokenValidTime = 1000L * 60 * 60 * 24; // 1일
    private final String secretKey = JwtSecret.JWT_SECRET_KEY;

    //JWT 생성
    public String createJwt(int userIdx) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type", "jwt")
                .claim("userIdx", userIdx)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    //JWT 검증
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    //JWT에서 유저 email 추출
    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    //JWT에서 userIdx 추출
    public Integer getUserIdx(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("userIdx", Integer.class);
    }


    //JWT에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        String userEmail = getUserEmail(token);
        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isEmpty()) {
            return null;
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(userOptional.get().getEmail())
                .password("")
                .build();

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //JWT를 HTTP 요청 헤더에서 추출하는 메서드 추가
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후의 토큰 값만 추출
        }
        return null;
    }
}