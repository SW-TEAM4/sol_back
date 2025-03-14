package org.team4.sol_server.domain.stamp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team4.sol_server.domain.stamp.entity.Stamp;
import org.team4.sol_server.domain.stamp.service.StampService;

import java.time.LocalDateTime;
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
@CrossOrigin(origins = "http://localhost:3000")  // 프론트엔드 URL 허용
public class StampController {

    @Autowired
    private StampService stampService;

    // 스탬프 저장 (하루에 한 번만 가능)
    @PostMapping("/save")
    public ResponseEntity<String> saveStamp(@RequestBody Map<String, Integer> request) {
        // 요청에서 "userIdx" 값을 안전하게 추출
        Object userIdxObj = request.get("userIdx");
        if (userIdxObj == null) {
            return ResponseEntity.badRequest().body("userIdx is missing");
        }

        Integer userIdx;
        try {
            userIdx = Integer.parseInt(userIdxObj.toString()); // 안전한 변환
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid userIdx format");
        }

        // 디버깅 로그 추가
        System.out.println("Received userIdx: " + userIdx);

        // 서비스 호출하여 스탬프 저장
        boolean result = stampService.saveStamp(userIdx);
        if (result) {
            return ResponseEntity.ok("스탬프 저장 성공");
        } else {
            System.out.println("Stamp save failed for userIdx: " + userIdx);
            return ResponseEntity.badRequest().body("스탬프 저장 실패");
        }
    }

    // 오늘 스탬프를 찍었는지 확인
    @GetMapping("/stamped/today/{userIdx}")
    public ResponseEntity<Map<String, Object>> checkStampedToday(@PathVariable int userIdx) {
        boolean hasStamped = stampService.hasStampedToday(userIdx);

        Map<String, Object> response = new HashMap<>();
        response.put("hasStamped", hasStamped);

        // 놓친 날에 대한 상태 추가
        int missedDays = stampService.calculateMissedDays(userIdx);
        response.put("missedDays", missedDays);

        return ResponseEntity.ok(response);
    }

    // 챌린지 시작 날짜 조회 (첫 번째 스탬프 찍은 날짜)
    @GetMapping("/startDate/{userIdx}")
    public ResponseEntity<?> getChallengeStartDate(@PathVariable int userIdx) {
        Optional<LocalDateTime> startDate = stampService.getChallengeStartDate(userIdx);

        if (startDate.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("startDate", startDate.get()); // "YYYY-MM-DDTHH:MM:SS" 형식 반환
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 사용자의 스탬프 기록 조회
    @GetMapping("/user/{userIdx}")
    public ResponseEntity<List<Map<String, Object>>> getStampsByUser(@PathVariable int userIdx) {
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

        return ResponseEntity.ok(response);
    }
}
