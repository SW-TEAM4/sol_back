package org.team4.sol_server.domain.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.team4.sol_server.domain.account.dto.AccountDTO;
import org.team4.sol_server.domain.account.dto.DepositRequestDTO;
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

//    // 이체 폼 페이지
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

}
