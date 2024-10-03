package com.sbackjung.transferstay.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {
    private final SecretKey secretKey;

    // 시크릿 키 생성
    public JwtUtils(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // 최신 방식으로 SecretKey 생성
    }

    // 토큰에서 이메일 추출
    public String getEmail(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("email", String.class);
    }

    // 토큰에서 사용자 ID 추출
    public Long getUserId(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("userId", Long.class);
    }

    // 토큰에서 역할(Role) 추출
    public String getRole(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("role", String.class);
    }

    // 토큰 만료 여부 확인
    public boolean isExpiredToken(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getExpiration().before(new Date());
    }

    // JWT 토큰 생성
    public String createJwtToken(Long userId, String email, String role, Long expiredMs) {
        return Jwts.builder()
            .claim("userId", userId)
            .claim("email", email)
            .claim("role", role)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
            .signWith(secretKey) // 서명
            .compact(); // 최종적으로 토큰 생성
    }
}
