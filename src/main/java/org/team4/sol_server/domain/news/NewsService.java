package org.team4.sol_server.domain.news;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class NewsService {

    private final String FASTAPI_BASE_URL = "http://localhost:8000";
    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

    /**
     * 실시간으로 뉴스 헤드라인 가져오기
     */
    public Map<String, Object> getHeadlines() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String url = FASTAPI_BASE_URL + "/news/headlines";
            logger.info("뉴스 헤드라인 요청: {}", url);

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            logger.info("뉴스 헤드라인 응답 상태 코드: {}", response.getStatusCode());

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("API 서버 오류: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("헤드라인 가져오기 중 예외 발생", e);
        }

        return Collections.singletonMap("headlines", new ArrayList<>());
    }

    /**
     * 실시간으로 기사 상세 내용 가져오기
     */
    public Map<String, Object> getArticle(String url) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            if (url == null || url.isEmpty()) {
                logger.error("기사 URL이 비어있습니다");
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "URL이 제공되지 않았습니다");
                return errorResponse;
            }

            String encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8);
            String apiUrl = FASTAPI_BASE_URL + "/news/article?url=" + encodedUrl;
            logger.info("기사 상세 요청: {}", apiUrl);

            ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);
            logger.info("기사 상세 응답 상태 코드: {}", response.getStatusCode());

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("API 서버 오류: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());

            // 오류 응답을 클라이언트에게 전달
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "기사를 찾을 수 없습니다");
            errorResponse.put("details", e.getResponseBodyAsString());
            return errorResponse;
        } catch (Exception e) {
            logger.error("기사 가져오기 중 예외 발생 (URL: {})", url, e);
        }

        Map<String, Object> fallbackResponse = new HashMap<>();
        fallbackResponse.put("title", "기사를 불러올 수 없습니다");
        fallbackResponse.put("content", "요청하신 기사를 불러오는 중 오류가 발생했습니다. 다른 기사를 선택해주세요.");
        return fallbackResponse;
    }
}

