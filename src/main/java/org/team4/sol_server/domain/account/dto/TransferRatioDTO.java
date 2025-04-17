package org.team4.sol_server.domain.account.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferRatioDTO {
    private String accountNumber;
    private int ratio;
}
