package com.sbackjung.transferstay.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="users")
public class UserDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;

    private String password;

    private String userName;

    @Column(length = 20)
    private String phone;

    private String nickName;

    private String oauthId;

    @Column(length = 50)
    private String oauthProvider;

    @Column(columnDefinition = "TEXT")
    private String accessToken;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private LocalDateTime agreementAt;

    @Column(length = 50)
    private String role;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long amount = 0L; // 초기 잔액을 0으로 설정

    public UserDomain(String userId, String email, String provider) {
        this.oauthId = userId;
        this.email = email;
        this.oauthProvider = provider;
    }
}
