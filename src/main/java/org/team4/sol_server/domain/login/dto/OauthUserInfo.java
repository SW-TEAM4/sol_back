package org.team4.sol_server.domain.login.dto;

public record OauthUserInfo(
        String oauthId,
        String nickname,
        String email,
        String gender,
        String birthday,
        String birthyear

){

}