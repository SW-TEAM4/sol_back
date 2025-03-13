package org.team4.sol_server.domain.login.dto;

public record OauthToken(
        String accessToken,
        String refreshToken
) {
}