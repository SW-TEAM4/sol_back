package org.team4.sol_server.domain.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
public class StockNewsController {

    @Autowired
    private StockNewsService stocknewsService;

    /**
     * 최신 뉴스 헤드라인 가져오기 (최대 limit 개수)
     * @param limit 가져올 뉴스 개수 (기본값: 7)
     * @return 최신 뉴스 리스트
     */
    @GetMapping("/headlines")
    public ResponseEntity<?> getHeadlines(
            @RequestParam(defaultValue = "7") int limit
    ) {
        List<StockNewsEntity> headlines = stocknewsService.getHeadlines(limit);
        if (headlines.isEmpty()) {
            // 뉴스가 없을 때 메시지를 포함한 응답 반환
            Map<String, String> response = Map.of("message", "뉴스가 없습니다");
            return ResponseEntity.ok(response);
        }
        System.out.println("헤드라인 데이터: " + headlines); // 디버깅 로그 추가
        return ResponseEntity.ok(headlines);
    }


}
