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
    public ResponseEntity<List<StockNewsEntity>> getHeadlines(
            @RequestParam(defaultValue = "7") int limit
    ) {
        List<StockNewsEntity> headlines = stocknewsService.getHeadlines(limit);
        if (headlines.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        System.out.println("헤드라인 데이터: " + headlines); // 디버깅 로그 추가
        return ResponseEntity.ok(headlines);
    }
}
