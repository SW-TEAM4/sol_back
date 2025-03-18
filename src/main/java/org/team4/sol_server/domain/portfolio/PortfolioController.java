package org.team4.sol_server.domain.portfolio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<Map<String, Object>> getAllPortfolios() {
        Map<String, Object> summary = portfolioService.getPortfolioSummary();
        return ResponseEntity.ok(summary);
    }
    @GetMapping("/accountInformation")
    public ResponseEntity<UserBalanceDTO> getAccountInformation(@RequestParam("userIdx")Long UserIdx) {
        UserBalanceDTO  userBalanceDTO = portfolioService.getAccountInformation(UserIdx);


        return ResponseEntity.ok(userBalanceDTO);
    }
}


