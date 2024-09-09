package com.sbackjung.transferstay.oauth2.handler;

import com.sbackjung.transferstay.domain.User;
import com.sbackjung.transferstay.jwt.JwtUtils;
import com.sbackjung.transferstay.oauth2.PrincipalDetails;
import com.sbackjung.transferstay.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtUtils jwtUtils;
  private final UserRepository userRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
    PrincipalDetails principalDetails = (PrincipalDetails) oAuth2Token.getPrincipal();

    // 사용자 정보로 JWT 토큰 생성
    User user = principalDetails.getUser();
    String jwtToken = jwtUtils.generateToken(user.getEmail(), user.getRole().name());

    // JWT 토큰을 헤더에 추가
    response.addHeader("Authorization", "Bearer " + jwtToken);

    // 기본 성공 URL로 리다이렉트
    response.sendRedirect("http://localhost:8080/auth/success");
  }
}