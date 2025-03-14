package org.team4.sol_server.domain.login;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * JWT를 이용한 사용자 정보 조회 API
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


    /**
     * 유저 기본 정보 입력 (최초 1회만 age, job 저장 / gender는 매번 업데이트)
     */
    @PatchMapping("/basic-info")
    public ResponseEntity<BaseResponse<BaseResponseStatus>> updateBasicInfo(HttpServletRequest request,
                                                                @RequestBody Map<String, Object> updates) throws BaseException {
        String token = jwtTokenProvider.resolveToken(request);
        Integer userIdx = jwtTokenProvider.getUserIdx(token);

        if (userIdx == null) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(BaseResponseStatus.INVALID_JWT));
        }

        BaseResponseStatus responseStatus = loginService.updateUserInfo(
                userIdx,
                (String) updates.get("gender"),
                (Integer) updates.get("age"),
                (Integer) updates.get("job")
        );

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.UPDATED_SUCCESS));
    }
    /**
     * 기본 정보 입력 여부 확인 API
     */
    @GetMapping("/check-basic-info")
    public ResponseEntity<BaseResponse<BaseResponseStatus>> checkBasicInfo(HttpServletRequest request) {
        try {
            // JWT 토큰에서 userIdx 추출
            String token = jwtTokenProvider.resolveToken(request);
            Integer userIdx = jwtTokenProvider.getUserIdx(token);

            if (userIdx == null) {
                return ResponseEntity.badRequest().body(new BaseResponse<>(BaseResponseStatus.INVALID_JWT));
            }

            boolean isBasicInfoCompleted = loginService.isBasicInfoCompleted(userIdx);

            if (isBasicInfoCompleted) {
                return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.BASIC_INFO_ALREADY_COMPLETED));
            } else {
                return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.BASIC_INFO_REQUIRED));
            }
        } catch (BaseException e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }
}

