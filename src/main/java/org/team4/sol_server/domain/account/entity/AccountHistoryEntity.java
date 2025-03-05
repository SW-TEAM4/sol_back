package org.team4.sol_server.domain.account.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account_history")
public class AccountHistoryEntity { // 거래 내역 저장
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_his_idx")
    private int id;

    // Q. account의 기본키를 외래키로 쓰는 건데, 보통 변수명 다르게 쓰나용? 백린이ㅠㅠ
    @ManyToOne
    @JoinColumn(name = "account_no", nullable = false)
    private AccountEntity account;

    // 아직 UserEntity 없음
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

    @Temporal(TemporalType.DATE)
    @Column(name = "created", nullable = false)
    private Date created;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated")
    private Date updated;
}
