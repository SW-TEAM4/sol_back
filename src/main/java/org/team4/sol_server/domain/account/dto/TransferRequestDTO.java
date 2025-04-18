package org.team4.sol_server.domain.account.dto;

import lombok.*;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequestDTO {
    private String fromAccount;   // 보내는계좌번호
    private String toAccount;     // 받는 계좌번호
    private Long amount;          // 금액
}
