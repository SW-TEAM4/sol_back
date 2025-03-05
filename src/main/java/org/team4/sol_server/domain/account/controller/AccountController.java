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
import org.team4.sol_server.domain.account.service.AccountService;

import java.util.Optional;

@RestController
@RequestMapping("api/account")
@RequiredArgsConstructor
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
        // 투자 비율 업데이트
        accountService.setTransferRatio(requestDTO.getAccountNumber(), requestDTO.getRatio());
        // 백엔드 응답(200)
        return ResponseEntity.ok("파킹 통장 투자 비율 업데이트 완료!!!");
    }

    // 이자 받기 --> Response(유저의 잔액 정보를 리턴)
    @PostMapping("/collect-interest") // (이자 받기 버튼 클릭 시)요청 받음
    // accountNumber --> 이자 받을 계좌 번호
    public ResponseEntity<AccountDTO> collectInterest(@RequestParam String accountNumber) {
        // collectInterest() --> 투자 비율을 기준으로 이자 계산 및 계좌 잔액 증가 함수
        AccountDTO updatedAccount = accountService.collectInterest(accountNumber);
        return ResponseEntity.ok(updatedAccount);
    }

    // 이자 정보 조회 --> 사용자가 받을 이자
    // 현재 잔액 기준으로 받을 이자 계산 후 반환
    @GetMapping("/get-interest") // Get <> 조회, 연동할 때 주의**(버튼 클릭 시가 아닌, 파킹 통장 화면 들어가자마자 나와야 함)
    public ResponseEntity<Integer> getInterest(@RequestParam String accountNumber) {
        // getInterest() --> 계좌의 이자 비율에 따라 이자 계산하는 함수
        int interest = accountService.getInterest(accountNumber);
        return ResponseEntity.ok(interest);
    }

}
