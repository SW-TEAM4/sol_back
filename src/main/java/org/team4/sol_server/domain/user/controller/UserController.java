package org.team4.sol_server.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
@CrossOrigin(origins = "http://localhost:3000")  // 프론트엔드 URL 허용
public class UserController {
    private final UserService userService;

    // 투자자 성향 분석 점수 저장 API
    @PostMapping("/{userIdx}/score")
    public ResponseEntity<String> savePersonalInvestor(@PathVariable int userIdx, @RequestBody Map<String, Integer> request) {
        int score = request.get("score");
        userService.savePersonalInvestor(userIdx, score);
        return ResponseEntity.ok("Score saved successfully");
    }

    // 투자 성향 점수 조회 API
    @GetMapping("/{userIdx}/score")
    public ResponseEntity<Integer> getPersonalInvestor(@PathVariable int userIdx) {
        int score = userService.getPersonalInvestor(userIdx);
        return ResponseEntity.ok(score);
    }

    // 사용자 이름 조회
    @GetMapping("/{userIdx}")
    public ResponseEntity<User> getUser(@PathVariable int userIdx) {
        User user = userService.getUserById(userIdx);
        if(user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
