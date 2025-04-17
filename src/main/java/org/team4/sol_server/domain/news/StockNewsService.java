package org.team4.sol_server.domain.news;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
@Service
public class StockNewsService {

    @Autowired
    private StockNewsRepository stocknewsRepository;

    /**
     * MySQL 데이터베이스에서 저장된 뉴스 헤드라인 가져오기 (최신순, 제한된 개수)
     * @param limit 가져올 뉴스 개수 (기본값: 10)
     * @return 최신 뉴스 리스트
     */
    public List<StockNewsEntity> getHeadlines(int limit) {
        // 최신순으로 정렬하고 최대 limit 개수만 반환
        return stocknewsRepository.findAll(
                PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "publicationDate"))
        ).getContent();
    }
}
