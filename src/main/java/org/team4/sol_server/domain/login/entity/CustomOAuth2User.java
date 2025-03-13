package org.team4.sol_server.domain.login.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {
    private final int userIdx; // ✅ userIdx 추가
    private final String username;
    private final String jwtToken;
    private final Map<String, Object> attributes; //OAuth2 데이터 저장

    public CustomOAuth2User(int userIdx, String username, String jwtToken, Map<String, Object> attributes) {
        this.userIdx = userIdx;
        this.username = username;
        this.jwtToken = jwtToken;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes; // OAuth2 데이터 반환
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return username;
    }
}
