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
//        @Id
//        @GeneratedValue(strategy = GenerationType.IDENTITY)
//        private Long id;
//
//        @Column(name = "account_number", unique = true, nullable = false)
//        private String accountNumber;
//
//        @Column(nullable = false)
//        private String name;
//
//        @Column(nullable = false)
//        private double balance;

        @Id
        @Column(name = "account_no", nullable = false, unique = true)
        private String accountNumber;

        // 아직 UserEntity 없음
//        @ManyToOne
//        @JoinColumn(name = "user_idx", nullable = false)
//        private UserEntity user;

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
