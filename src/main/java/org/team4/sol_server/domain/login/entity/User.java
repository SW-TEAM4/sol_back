package org.team4.sol_server.domain.login.entity;


import jakarta.persistence.*;
import lombok.*;
import org.team4.sol_server.domain.login.dto.OauthUserInfo;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userIdx;

    @Column(nullable = false)
    private String loginType; //kakao 또는 naver 저장

    @Column(name = "user_email", unique = true, nullable = true)
    private String email;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "user_gender", nullable = true)
    private String gender;

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
     * 최초 1회만 age, job 저장
     * gender는 매번 업데이트
     */
    public void updateUserFirstTime(String gender, Integer age, Integer job) {
        if (gender != null) {
            this.gender = gender; // gender는 무조건 업데이트
        }
        if (this.age == null && age != null) {
            this.age = age; // 최초 1회만 저장
        }
        if (this.job == null && job != null) {
            this.job = job; // 최초 1회만 저장
        }
        this.updated = LocalDateTime.now();
    }




}