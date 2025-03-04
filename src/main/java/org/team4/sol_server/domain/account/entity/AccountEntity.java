package org.team4.sol_server.domain.account.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account")
public class AccountEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "account_number", unique = true, nullable = false)
        private String accountNumber;

        @Column(nullable = false)
        private String name;

        @Column(nullable = false)
        private double balance;
}
