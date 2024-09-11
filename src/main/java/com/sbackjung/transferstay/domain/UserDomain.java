package com.sbackjung.transferstay.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    @Column(length = 36)
    private String userUuid;

    @Column(length = 100)
    private String userName;

    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 2083)
    private String imageUrl;

    private String password;

    @Column(length = 50)
    private String oauthProvider;

    private String oauthId;

    @Column(columnDefinition = "TEXT")
    private String accessToken;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Column(length = 50)
    private String role;

    public UserDomain(String userId, String email, String provider) {
        this.oauthId = userId;
        this.email = email;
        this.oauthProvider = provider;
    }
}
