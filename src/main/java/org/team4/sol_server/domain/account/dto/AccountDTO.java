package org.team4.sol_server.domain.account.dto;

import lombok.*;

import java.math.BigInteger;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private String accountNumber;
    private Long balance;
}
