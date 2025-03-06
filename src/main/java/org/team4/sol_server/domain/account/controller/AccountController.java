package org.team4.sol_server.domain.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.team4.sol_server.domain.account.dto.AccountDTO;
import org.team4.sol_server.domain.account.dto.DepositRequestDTO;
import org.team4.sol_server.domain.account.dto.TransferRatioDTO;
import org.team4.sol_server.domain.account.dto.TransferRequestDTO;
import org.team4.sol_server.domain.account.entity.AccountEntity;
import org.team4.sol_server.domain.account.entity.AccountHistoryEntity;
import org.team4.sol_server.domain.account.repository.AccountRepository;
import org.team4.sol_server.domain.account.service.AccountService;

import java.util.List;
import java.util.Optional;

/*
파일명 : AccountController.java
생성자 : JDeok
날 짜  : 2025.03.05
시 간  : 오후 02:14
기 능  : 계좌페이지
Params :
Return :
변경사항
     - 2025.03.05 : JDeok(최초작성)
*/
@RestController
@RequestMapping("api/account")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {

    private final AccountService accountService;

    // 계좌 조회 페이지
    @GetMapping("/balance")
    public ResponseEntity<AccountDTO> getAccount(@RequestParam String accountNumber, Model model) {
        Optional<AccountEntity> account = accountService.getAccountByNumber(accountNumber);
        account.ifPresent(value -> model.addAttribute("account", value));

        AccountDTO dto = AccountDTO.builder()
                .accountNumber(account.get().getAccountNumber())
                .balance(account.get().getBalance())
                .build();

        return ResponseEntity.ok().body(dto);
    }

//    이체 폼 페이지
//    @GetMapping("/transfer")
//    public String showTransferForm() {
//        return "transfer";
//    }

    // 이체 기능 수행
    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequestDTO transferRequestDTO, Model model) {
        boolean success = accountService.transfer(transferRequestDTO.getFromAccount()
                                                , transferRequestDTO.getToAccount()
                                                , transferRequestDTO.getAmount());
        model.addAttribute("success", success);
        return "transfer_result";
    }

    // 입금 (Deposit)
    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody DepositRequestDTO requestDTO) {

        boolean success = accountService.deposit(requestDTO.getAccountNumber(), requestDTO.getAmount());
        if (success) {
            return ResponseEntity.ok("Deposit successful");
        }
        return ResponseEntity.badRequest().body("Deposit failed: Invalid account or amount");
    }

    // 증권 계좌 이체 비율 설정
    @PostMapping("/set-ratio")
    // accountNumber --> 계좌 번호 받음, ratio --> 이체 비율(%) 받음
    public ResponseEntity<String> setTransferRatio(@RequestBody TransferRatioDTO requestDTO) {
        accountService.setTransferRatio(requestDTO.getAccountNumber(), requestDTO.getRatio()); // 투자 비율 업데이트
        return ResponseEntity.ok("파킹 통장 투자 비율 업데이트 완료!!!");
    }

    // 증권 계좌 이체 비율 조회
    @GetMapping("/get-ratio")
    public Integer getTransferRatio(@RequestParam String accountNumber) {
        return accountService.getTransferRatio(accountNumber);
    }

    // 이자 받기
    @PostMapping("/collect-interest")
    public ResponseEntity<AccountDTO> collectInterest(@RequestParam String accountNumber) {
        AccountDTO updatedAccount = accountService.collectInterest(accountNumber); // 투자 비율을 기준으로 이자 계산 및 계좌 잔액 증가 함수
        return ResponseEntity.ok(updatedAccount);
    }

    // 이자 정보 조회 --> 사용자가 받을 이자
    // 현재 잔액 기준으로 받을 이자 계산 후 반환
    @GetMapping("/get-interest")
    public ResponseEntity<Integer> getInterest(@RequestParam String accountNumber) {
        int interest = accountService.getInterest(accountNumber); // 이자 비율에 따라 이자 계산
        return ResponseEntity.ok(interest);
    }

    // 해당 계좌 통장 내역 조회
    @GetMapping("/history")
    public ResponseEntity<List<AccountHistoryEntity>> getAccountHistory(@RequestParam String accountNumber) {
        List<AccountHistoryEntity> historyList = accountService.getAccountHistory(accountNumber);
        return ResponseEntity.ok(historyList);
    }

}
