package org.team4.sol_server.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.team4.sol_server.domain.stamp.entity.Stamp;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class stamp_User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userIdx;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private char userGender; // 0 : 남성, 1 : 여성

    @Column(nullable = false)
    private int personalInvestor;

    @Column(nullable = false)
    private String birthday;

    @Column(nullable = false)
    private String birthYear;

    @Column(nullable = false)
    private String loginType;

    @Column(nullable = false)
    private String jwtToken;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private int job;

//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//    private List<Stamp> stamps;  // User와 Stamp의 관계

    public String getInvestmentType() {
        if(personalInvestor <= 5) {
            return "완숙이 (저위험)";
        } else if(personalInvestor <= 10) {
            return "차철이 (중위험)";
        } else {
            return "열식이 (고위험)";
        }
    }
}
