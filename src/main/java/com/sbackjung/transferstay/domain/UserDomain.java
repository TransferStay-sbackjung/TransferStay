package com.sbackjung.transferstay.domain;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name="user")
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

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    @Column(length = 50)
    private String role;
}
