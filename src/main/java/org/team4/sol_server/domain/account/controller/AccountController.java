package org.team4.sol_server.domain.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team4.sol_server.domain.account.dto.*;
import org.team4.sol_server.domain.account.entity.AccountEntity;
import org.team4.sol_server.domain.account.service.AccountHistoryService;
import org.team4.sol_server.domain.account.service.AccountService;

import java.util.List;
import java.util.Map;
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
    private final AccountHistoryService accountHistoryService;


    // 계좌 조회 페이지
    @GetMapping("/balance")
    public ResponseEntity<AccountDTO> getAccount(@RequestParam String accountNumber) {
        Optional<AccountEntity> account = accountService.getAccountByNumber(accountNumber);

        // 계좌번호가 없으면 400 응답 반환
        if(account.isEmpty()){
            return ResponseEntity.badRequest().body(null);
        }

        AccountDTO dto = AccountDTO.builder()
                .accountNumber(account.get().getAccountNumber())
                .balance(account.get().getBalance())
                .build();

        return ResponseEntity.ok().body(dto);
    }

    // 이체 기능 수행
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequestDTO transferRequestDTO) {
        System.out.println("받은 요청 데이터 : " + transferRequestDTO);
        String result = accountService.transfer(transferRequestDTO.getFromAccount()
                                                , transferRequestDTO.getToAccount()
                                                , transferRequestDTO.getAmount());

         if (result.equals("잔액이 부족합니다.")) {
            return ResponseEntity.status(400).body(result); //  400 Bad Request
        }

        return ResponseEntity.ok(result);
    }
    // 계좌 체크
    @GetMapping("/check-accounts")
    public ResponseEntity<String> checkAccounts(@RequestParam String fromAccount, @RequestParam String toAccount) {
        String checkResult = accountService.checkAccounts(fromAccount, toAccount);

        if(!checkResult.equals("VALID")){
            return ResponseEntity.status(400).body(checkResult);
        }
        return ResponseEntity.ok("계좌가 확인되었습니다.");
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
        return ResponseEntity.ok("증권 계좌 이체 비율 설정: " + requestDTO.getRatio());
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

    @GetMapping("/history")
    public ResponseEntity<List<Map<String, Object>>> getAccountHistory(
            @RequestParam String accountNumber) {
        List<Map<String, Object>> history = accountHistoryService.getTransactionHistory(accountNumber);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/add-transaction")
    public ResponseEntity<String> addTransaction(
            @RequestParam String accountNumber,
            @RequestParam int amount,
            @RequestParam String desWitType,
            @RequestParam String displayName) {

        accountService.addTransaction(accountNumber, amount, desWitType, displayName);
        return ResponseEntity.ok("거래 내역 추가 및 잔액 업데이트 완료");
    }

    /** 계좌이력테이블 insert **/
    @PostMapping("/insertAccountHistroy")
    public ResponseEntity<String> createAccount(@RequestBody InsertAccountHistroy insertAccountHistroy) {
        System.out.println("받은 요청 데이터 : " + insertAccountHistroy);
        String result = accountService.insertAccountHistroy(
                  insertAccountHistroy.getFromAccount()      // 계좌번호
                , insertAccountHistroy.getReceiverName()     // 보내는사람
                , insertAccountHistroy.getAmount()           // 보내는 금액
                , insertAccountHistroy.getPrebalance()       // 현재잔고금액
                , insertAccountHistroy.getDesWitType());     // 입,출금

        return ResponseEntity.ok(result);
    }

    /* 고객 명 찾기 */
    @GetMapping("/{accountNo}/user-name")
    public String getUserName(@PathVariable String accountNo) {
        return accountService.getUserNameByAccountNo(accountNo);
    }

    /**
     * user_idx로  account_No 가져오기
     **/
    @GetMapping("/getAccountNo")
    public Optional<AccountEntity> getAccountNo(@RequestParam int userIdx) {
        return accountService.getAccountNo(userIdx);
    }
}
