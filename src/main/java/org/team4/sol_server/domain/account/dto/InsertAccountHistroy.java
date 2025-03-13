package org.team4.sol_server.domain.account.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InsertAccountHistroy {
    private String fromAccount;   // 보내는계좌번호   account_no
    private String receiverName;  // 보낸사람        display_name
    private Long   amount;        // 보내는 금액     transfer_balance
    private Long   prebalance;    // 보낸 후 잔고금액 pre_balance
    private String desWitType;    // 입,출금         des_wit_type


}
