package org.team4.sol_server.domain.login.entity;


import jakarta.persistence.*;
import lombok.*;
import org.team4.sol_server.domain.login.dto.OauthUserInfo;
import org.team4.sol_server.domain.stamp.entity.Stamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private int userIdx;

    @Column(nullable = false)
    private String loginType; //kakao 또는 naver 저장

    @Column(name = "user_email", unique = true, nullable = true)
    private String email;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "user_gender", nullable = true)
    private String gender;

    @Column(nullable = false)
    private int personalInvestor;

    @Column(nullable = true)
    private String birthday;

    @Column(nullable = true)
    private String birthyear;

    @Column(nullable = true)
    private Integer age; // null값 허용 때문에 Integer 씀

    @Column(nullable = true)
    private Integer job;

    @Column
    private LocalDateTime created;

    @Column
    private LocalDateTime updated;

    @Column(name = "jwt_token", nullable = true)
    private String jwtToken;

    public void updateJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }


    public void updateTime() {
        this.updated = LocalDateTime.now();
    }

    public static User from(OauthUserInfo userInfo, String loginType) {
        return User.builder()
                .username(userInfo.nickname())
                .email(userInfo.email())
                .gender(userInfo.gender())  //OauthUserInfo에 gender 필드가 있다고 가정
                .birthday(userInfo.birthday())
                .birthyear(userInfo.birthyear())
                .loginType(loginType)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();
    }

    /**
     * 모든 필드를 업데이트 (조건 없이)
     */
    public boolean updateUser(String gender, Integer age, Integer job) {
        boolean isUpdated = false;

        // gender, age, job을 모두 조건 없이 업데이트
        if (gender != null) {
            this.gender = gender; // gender 업데이트
            isUpdated = true;
        }

        if (age != null) {
            this.age = age; // age 업데이트
            isUpdated = true;
        }

        if (job != null) {
            this.job = job; // job 업데이트
            isUpdated = true;
        }

        if (isUpdated) {
            this.updated = LocalDateTime.now(); // 업데이트가 발생하면 updated 시간 갱신
        }

        return isUpdated; // 업데이트가 발생했으면 true, 아니면 false 반환
    }

    //stamp
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Stamp> stamps;  // User와 Stamp의 관계





}