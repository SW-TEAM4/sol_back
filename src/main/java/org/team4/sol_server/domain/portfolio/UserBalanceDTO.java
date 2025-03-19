package org.team4.sol_server.domain.portfolio;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBalanceDTO {
    private Long balance;
    private String userName;

    @JsonInclude(JsonInclude.Include.ALWAYS) // null 값도 포함
    private int  personalInvestor;

}
