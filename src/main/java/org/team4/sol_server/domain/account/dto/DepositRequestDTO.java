package org.team4.sol_server.domain.account.dto;
import lombok.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositRequestDTO {

    @NotNull
    private String accountNumber;

    @NotNull
    private Long amount;
}
