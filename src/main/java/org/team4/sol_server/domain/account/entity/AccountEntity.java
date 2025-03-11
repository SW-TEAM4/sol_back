package org.team4.sol_server.domain.account.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account")
public class AccountEntity extends BaseEntity {

//        @ManyToOne
//        @JoinColumn(name = "user_idx", nullable = false)
//        private UserEntity user;

        @Id
        @Column(name = "account_no", nullable = false, unique = true)
        private String accountNumber;

        // 임시
        @Column(name = "user_idx", nullable = false)
        private int userIdx;

        @Column(name = "balance", nullable = false)
        private Long balance;

        @Column(name = "investor_ratio", nullable = false)
        private int investorRatio;  // 이체 비율

        @Column(name = "interest", nullable = false)
        private int interest;  // 이자 금액

        @Column(name = "interest_ratio", nullable = false)
        private double interestRatio;  // 이자율
}
