package org.team4.sol_server.domain.login.dto;


public record OauthResponse(
        String oauthId,
        String nickname,
        String email,
        String gender,
        String birthday,
        String birthyear,
        String accessToken,
        String refreshToken
) {
    public static OauthResponse of(
            String oauthId,
            String nickname,
            String email,
            String gender,
            String birthday,
            String birthyear,
            String accessToken,
            String refreshToken
    ) {
        return new OauthResponse(oauthId, nickname, email, gender, birthday, birthyear, accessToken, refreshToken);
    }
}
