package org.team4.sol_server.domain.stamp.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team4.sol_server.config.BaseException;
import org.team4.sol_server.config.BaseResponse;
import org.team4.sol_server.config.BaseResponseStatus;
import org.team4.sol_server.config.jwt.JwtTokenProvider;
import org.team4.sol_server.domain.stamp.entity.Stamp;
import org.team4.sol_server.domain.stamp.service.StampService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/*
파일명 : StampController.java
생성자 : JM
날 짜  : 2025.03.10
시 간  : 오전 11:00
기 능  : 유저 정보
Params :
Return :
변경사항
     - 2025.03.10 : JM(최초작성)
*/


@RestController
@RequestMapping("/api/stamp")
@RequiredArgsConstructor
public class StampController {

    @Autowired
    private StampService stampService;
    private final JwtTokenProvider jwtTokenProvider;

    // 스탬프 저장 (하루에 한 번만 가능)
    @PostMapping("/save")
    public ResponseEntity<BaseResponse<BaseResponseStatus>> saveStamp(HttpServletRequest request) {
        try {
            // JWT 토큰에서 userIdx 추출
            String token = jwtTokenProvider.resolveToken(request);
            Integer userIdx = jwtTokenProvider.getUserIdx(token);

            if (userIdx == null) {
                return ResponseEntity.badRequest().body(new BaseResponse<>(BaseResponseStatus.INVALID_JWT));
            }

            // 디버깅 로그 추가
            System.out.println("Received userIdx from JWT token: " + userIdx);

            // 서비스 호출하여 스탬프 저장
            boolean result = stampService.saveStamp(userIdx);
            if (result) {
                return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.STAMP_SAVE));
            } else {
                System.out.println("Stamp save failed for userIdx: " + userIdx);
                return ResponseEntity.badRequest().body(new BaseResponse<>(BaseResponseStatus.STAMP_NOT_SAVE));
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(BaseResponseStatus.STAMP_NOT_SAVE));
        }
    }


    // 오늘 스탬프를 찍었는지 확인
    @GetMapping("/stamped/today")
    public ResponseEntity<BaseResponse<BaseResponseStatus>> checkStampedToday(HttpServletRequest request) {
        try {
            // JWT 토큰에서 userIdx 추출
            String token = jwtTokenProvider.resolveToken(request);
            Integer userIdx = jwtTokenProvider.getUserIdx(token);

            if (userIdx == null) {
                return ResponseEntity.badRequest().body(new BaseResponse<>(BaseResponseStatus.INVALID_JWT));
            }

            // 사용자가 오늘 스탬프를 찍었는지 확인
            boolean hasStamped = stampService.hasStampedToday(userIdx);

            if (!hasStamped) {
                // 스탬프가 없을 경우 BaseException 던지기
                throw new BaseException(BaseResponseStatus.STAMP_NOT_YET);
            }

            // 놓친 날에 대한 상태 추가
            int missedDays = stampService.calculateMissedDays(userIdx);

            // 응답에 스탬프 상태와 놓친 날 수 추가
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.STAMP_HISTORY));

        } catch (BaseException e) {
            // BaseException을 처리하여 응답 반환
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        }
    }


    // 챌린지 시작 날짜 조회 (첫 번째 스탬프 찍은 날짜)
    @GetMapping("/startDate")
    public ResponseEntity<BaseResponse<Map<String, Object>>> getChallengeStartDate(HttpServletRequest request) {
        try {
            // JWT 토큰에서 userIdx 추출
            String token = jwtTokenProvider.resolveToken(request);
            Integer userIdx = jwtTokenProvider.getUserIdx(token);

            if (userIdx == null) {
                return ResponseEntity.badRequest().body(new BaseResponse<>(BaseResponseStatus.INVALID_JWT));
            }

            // 디버깅 로그 추가
            System.out.println("Received userIdx from JWT token: " + userIdx);

            // 서비스 호출하여 챌린지 시작 날짜 조회
            Optional<LocalDateTime> startDate = stampService.getChallengeStartDate(userIdx);

            if (startDate.isPresent()) {
                String formattedStartDate = startDate.get().truncatedTo(ChronoUnit.SECONDS).toString();  // LocalDateTime을 ISO 8601 형식으로 변환, 나노 초 제외
                Map<String, Object> response = new HashMap<>();
                response.put("startDate", formattedStartDate); // "YYYY-MM-DDTHH:MM:SS" 형식 반환
                return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.STAMP_TIME, response));
            } else {
                return ResponseEntity.badRequest().body(new BaseResponse<>(BaseResponseStatus.STAMP_NOT_YET));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(BaseResponseStatus.STAMP_NOT_YET));
        }
    }


    // 사용자의 스탬프 기록 조회
    @GetMapping("/user")
    public ResponseEntity<BaseResponse<List<Map<String, Object>>>> getStampsByUser(HttpServletRequest request) {
        try {
            // JWT 토큰에서 userIdx 추출
            String token = jwtTokenProvider.resolveToken(request);
            Integer userIdx = jwtTokenProvider.getUserIdx(token);

            if (userIdx == null) {
                return ResponseEntity.badRequest().body(new BaseResponse<>(BaseResponseStatus.INVALID_JWT));  // userIdx가 없으면 400 응답
            }

            // 스탬프 목록 조회
            List<Stamp> stamps = stampService.getStampsByUser(userIdx);

            // 클라이언트에서 필요한 데이터만 전달
            List<Map<String, Object>> response = stamps.stream()
                    .map(stamp -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("stampIdx", stamp.getStampIdx());  // 스탬프 ID
                        map.put("day", stamp.getDay());           // 1~30일 범위의 날짜
                        map.put("isStamped", stamp.isStamped());  // 스탬프 여부
                        return map;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.STAMP_HISTORY, response));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(BaseResponseStatus.STAMP_NOT_YET));  // 예외 발생 시 400 응답
        }
    }
}
