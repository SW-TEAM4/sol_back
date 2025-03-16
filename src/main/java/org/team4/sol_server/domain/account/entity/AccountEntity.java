package org.team4.sol_server.domain.account.entity;

import jakarta.persistence.*;
import lombok.*;
import org.team4.sol_server.domain.login.entity.User;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account")
public class AccountEntity extends BaseEntity {

        @Id
        @Column(name = "account_no", nullable = false, unique = true)
        private String accountNumber;

        @ManyToOne
        @JoinColumn(name = "user_idx", referencedColumnName = "user_idx")
        private User user;

        @Column(name = "balance", nullable = false)
        private Long balance = 0L;

        @Column(name = "investor_ratio", nullable = false)
        private int investorRatio;  // 이체 비율

        @Column(name = "interest", nullable = false)
        private int interest;  // 이자 금액

        @Column(name = "interest_ratio", nullable = false)
        private double interestRatio;  // 이자율
}
