package com.sbackjung.transferstay.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {
    private SecretKey secretKey;

    // 시크릿 키 생성
    public JwtUtils(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    public String getUserId(String token){
        return Jwts.parser().verifyWith(secretKey)
                .build().parseSignedClaims(token).getPayload()
                .get("userId",String.class);
    }

    public String getRole(String token){
        String role = Jwts.parser().verifyWith(secretKey)
                .build().parseSignedClaims(token).getPayload()
                .get("role",String.class);
        return role;
    }

    public boolean isExpiredToken(String token){
        return Jwts.parser().verifyWith(secretKey)
                .build().parseSignedClaims(token)
                .getPayload().getExpiration().before(new Date());
    }

    public String createJwtToken(String userId,String role,Long expiredMs){
        return Jwts.builder()
                .claim("userId",userId)
                .claim("role",role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
