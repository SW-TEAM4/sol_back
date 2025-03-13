package org.team4.sol_server.domain.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/headlines")
    public ResponseEntity<Map<String, Object>> getHeadlines() {
        Map<String, Object> headlines = newsService.getHeadlines();
        return ResponseEntity.ok(headlines);
    }

    @GetMapping("/article")
    public ResponseEntity<Map<String, Object>> getArticle(@RequestParam String url) {
        Map<String, Object> article = newsService.getArticle(url);
        if (article.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(article);
    }
}


