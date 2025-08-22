package com.tunaforce.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_user")
public class User extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", unique = true, nullable = false, updatable = false)
    private UUID userId;

    @NotBlank
    @Column(name = "user_login_id", unique = true, nullable = false, updatable = false)
    private String userLoginId;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "slack_id")
    private String slackId;

    @NotBlank
    @Column(nullable = false)
    private String tel;

    @Builder
    public User(String userLoginId, String username, String password, UserRole role, String slackId, String tel) {
        this.userLoginId = userLoginId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.slackId = slackId;
        this.tel = tel;
    }

    /**
     * 사용자 정보 업데이트: 비밀번호는 null/blank가 아니면 교체
     */
    public void updateInfo(String username, String encodedPasswordOrNull, UserRole role, String slackId, String tel) {
        if (username != null && !username.isBlank()) {
            this.username = username;
        }
        if (encodedPasswordOrNull != null && !encodedPasswordOrNull.isBlank()) {
            this.password = encodedPasswordOrNull;
        }
        if (role != null) {
            this.role = role;
        }
        this.slackId = slackId; // nullable allowed
        if (tel != null && !tel.isBlank()) {
            this.tel = tel;
        }
    }
}
