package org.team4.sol_server.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team4.sol_server.config.BaseResponse;
import org.team4.sol_server.config.BaseResponseStatus;
import org.team4.sol_server.config.jwt.JwtTokenProvider;
import org.team4.sol_server.domain.login.entity.User;
import org.team4.sol_server.domain.login.entity.User;
import org.team4.sol_server.domain.user.service.UserService;

import java.util.Map;

/*
파일명 : UserController.java
생성자 : JM
날 짜  : 2025.03.01
시 간  : 오후 03:00
기 능  : 유저 정보
Params :
Return :
변경사항
     - 2025.03.01 : JM(최초작성)
*/

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")  // 프론트엔드 URL 허용
public class UserController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtTokenProvider jwtTokenProvider;

    // 투자자 성향 분석 점수 저장 API
    @PostMapping("/score")
    public ResponseEntity<BaseResponse<BaseResponseStatus>> savePersonalInvestor(HttpServletRequest request, @RequestBody Map<String, Integer> requestBody) {
        // JWT 토큰에서 userIdx 추출
        String token = jwtTokenProvider.resolveToken(request);
        Integer userIdx = jwtTokenProvider.getUserIdx(token);

        if (userIdx == null) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(BaseResponseStatus.INVALID_JWT));
        }

        // 요청 본문에서 score 추출
        int score = requestBody.get("score");

        try {
            // 사용자 점수 저장
            userService.savePersonalInvestor(userIdx, score);

            // 점수 저장 성공 시 응답 반환
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SCORE_SAVE_SUCCESS));
        } catch (Exception e) {
            // 점수 저장 실패 시 응답 반환
            return ResponseEntity.badRequest().body(new BaseResponse<>(BaseResponseStatus.SCORE_SAVE_FAILED));
        }
    }

    // 투자 성향 점수 조회 API
    @GetMapping("/score")
    public ResponseEntity<BaseResponse<Integer>> getPersonalInvestor(HttpServletRequest request) {
        try {
            // JWT 토큰에서 userIdx 추출
            String token = jwtTokenProvider.resolveToken(request);
            Integer userIdx = jwtTokenProvider.getUserIdx(token);

            if (userIdx == null) {
                // JWT가 없으면 실패 응답 반환
                return ResponseEntity.badRequest().body(new BaseResponse<>(BaseResponseStatus.INVALID_JWT));
            }

            // 사용자 점수 조회
            int score = userService.getPersonalInvestor(userIdx);

            // 점수 조회 성공 시 응답 반환
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SCORE_FETCH_SUCCESS, score));
        } catch (Exception e) {
            // 점수 조회 실패 시 응답 반환
            return ResponseEntity.badRequest().body(new BaseResponse<>(BaseResponseStatus.SCORE_FETCH_FAILED));
        }
    }


    // 사용자 이름 조회
    @GetMapping("/user")
    public ResponseEntity<BaseResponse<User>> getUser(HttpServletRequest request) {
        try {
            // JWT 토큰에서 userIdx 추출
            String token = jwtTokenProvider.resolveToken(request);
            Integer userIdx = jwtTokenProvider.getUserIdx(token);

            if (userIdx == null) {
                // JWT가 없으면 실패 응답 반환
                return ResponseEntity.badRequest().body(new BaseResponse<>(BaseResponseStatus.INVALID_JWT));
            }

            // 사용자 조회
            User user = userService.getUserById(userIdx);

            if (user != null) {
                // 사용자 조회 성공 시 응답 반환
                return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.USER_NAME_FETCH_SUCCESS, user));
            } else {
                // 사용자 찾지 못한 경우
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse<>(BaseResponseStatus.USER_NAME_FETCH_FAILED, null));
            }
        } catch (Exception e) {
            // 예외 발생 시 실패 응답 반환
            return ResponseEntity.badRequest().body(new BaseResponse<>(BaseResponseStatus.USER_NAME_FETCH_FAILED, null));
        }
    }


}
