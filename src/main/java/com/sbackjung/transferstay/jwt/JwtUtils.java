package com.sbackjung.transferstay.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.token-validity-in-seconds}")
  private long tokenValidityInSeconds;

  public String generateToken(String email, String role) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + tokenValidityInSeconds * 1000);

    return Jwts.builder()
        .setSubject(email)
        .claim("role", role)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public String getEmailFromToken(String token) {
    Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    return claims.getSubject();
  }
}
