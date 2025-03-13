package org.team4.sol_server.domain.login;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.team4.sol_server.config.BaseException;
import org.team4.sol_server.config.BaseResponse;
import org.team4.sol_server.config.BaseResponseStatus;
import org.team4.sol_server.config.jwt.JwtTokenProvider;
import org.team4.sol_server.domain.login.service.LoginService;
import java.util.Map;

import static org.team4.sol_server.config.BaseResponseStatus.SUCCESS;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;


    /**
     *  JWT를 이용한 사용자 정보 조회 API
     */
    @GetMapping("/user/me")
    public BaseResponse<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        try {
            // 서비스 호출 (JWT에서 사용자 정보 가져오기)
            Map<String, Object> userInfo = loginService.getUserInfoFromToken(request);
            return new BaseResponse<>(userInfo);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * JWT에서 userIdx 추출 API
     */
    @GetMapping("/user/userIdx")
    public BaseResponse<Integer> getUserIdxFromJwt(@CookieValue(value = "jwtToken", required = false) String jwtToken) {
        try {
            if (jwtToken == null) {
                return new BaseResponse<>(BaseResponseStatus.EMPTY_JWT);
            }

            int userIdx = jwtTokenProvider.getUserIdx(jwtToken);
            return new BaseResponse<>(userIdx);
        } catch (Exception e) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_JWT);
        }
    }
}



    /**
     * JWT 검증 후 사용자 정보 반환 API
     */
//    @PostMapping("/user/jwt")
//    public BaseResponse<Map<String, String>> getJwtByUserInfo(
//            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
//            @RequestBody Map<String, String> loginRequest) {
//
//        try {
//            String email = loginRequest.get("email");
//
//            Map<String, String> result = loginService.getJwtByUserInfo(authorizationHeader, email);
//
//            return new BaseResponse(SUCCESS);
//        } catch (BaseException e) {
//            return new BaseResponse<>(e.getStatus());
//        }
//    }

