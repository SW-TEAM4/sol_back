package org.team4.sol_server.domain.account.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account_history")
public class AccountHistoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_his_idx")
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_no", nullable = false)
    private AccountEntity account;

//    @ManyToOne
//    @JoinColumn(name = "user_idx", nullable = false)
//    private UserEntity user;

    // 임시
    @Column(name = "user_idx", nullable = false)
    private int userIdx;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "pre_balance", nullable = false)
    private int preBalance;

}
