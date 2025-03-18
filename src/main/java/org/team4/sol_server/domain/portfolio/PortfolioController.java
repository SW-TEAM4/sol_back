package org.team4.sol_server.domain.portfolio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/list")
    public ResponseEntity<List<PortfolioEntity>> getAllPortfolios(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        System.out.println("Authorization Header: " + authHeader); // 디버깅용
        List<PortfolioEntity> portfolioList = portfolioService.getAllPortfolios();
        return ResponseEntity.ok(portfolioList);
    }

    @GetMapping("/accountInformation")
    public ResponseEntity<UserBalanceDTO> getAccountInformation(@RequestParam("userIdx")Long userIdx) {
        UserBalanceDTO  userBalanceDTO = portfolioService.getAccountInformation(userIdx);


        return ResponseEntity.ok(userBalanceDTO);
    }
}

