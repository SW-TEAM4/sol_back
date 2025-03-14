package org.team4.sol_server.domain.stamp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.team4.sol_server.domain.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@EntityListeners(value = {AuditingEntityListener.class})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")  // 순환 참조 방지
@JsonIgnoreProperties("user")  // JSON 직렬화 시 user 필드 무시
public class Stamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stampIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    @JsonIgnoreProperties("stamps")  // User 직렬화 시 stamps 포함 안 함
    private User user;  // user와 조인

    private int day;  // 1일, 2일, ..., 30일

    private boolean isStamped; // 스탬프 여부

    @CreatedDate
    @Column(name = "created", updatable = false)
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "updated")
    private LocalDateTime updated;
}
