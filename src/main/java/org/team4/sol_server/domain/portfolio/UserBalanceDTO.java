package org.team4.sol_server.domain.portfolio;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigInteger;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBalanceDTO {
    private Long balance;
    private String userName;
    private int personalInvestor;

}
