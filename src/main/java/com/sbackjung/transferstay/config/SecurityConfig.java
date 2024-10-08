package com.sbackjung.transferstay.config;

import com.sbackjung.transferstay.handler.OAuth2SuccessHandler;
import com.sbackjung.transferstay.jwt.JwtFilter;
import com.sbackjung.transferstay.jwt.JwtUtils;
import com.sbackjung.transferstay.jwt.LoginFilter;
import com.sbackjung.transferstay.service.EmailLoginService;
import com.sbackjung.transferstay.service.SocialLoginService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtUtils jwtUtils;
  private final SocialLoginService socialLoginService;
  private final OAuth2SuccessHandler oAuth2SuccessHandler;
  private final EmailLoginService emailLoginService;
  private final PasswordEncoder passwordEncoder;

  @Bean
  public SecurityFilterChain configure(HttpSecurity http, AuthenticationManager authManager) throws Exception {
    LoginFilter loginFilter = new LoginFilter(authManager, jwtUtils);
    loginFilter.setFilterProcessesUrl("/email-login");

    http
        .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .formLogin(auth -> auth.disable())
        .httpBasic(auth -> auth.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
        .authorizeHttpRequests(request -> request
            .requestMatchers("/","/check-db", "/login/oauth2/**", "/h2" +
                            "-console/**",
                    "/auth/**","/api/test").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/assignment-posts" +
                    "/{postId}", "/api/v1/assignment-posts", "/api/v1/search"
                    ,"/api/v1/auction").permitAll()
            .requestMatchers(HttpMethod.POST,"/email-login", "/api/v1/user/join", "/api/v1/email/**").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .anyRequest().authenticated()
        )
        .oauth2Login(oauth2 -> oauth2
            .redirectionEndpoint(endpoint -> endpoint.baseUri("/login/oauth2/code/{registrationId}"))
            .userInfoEndpoint(endpoint -> endpoint.userService(socialLoginService))
            .successHandler(oAuth2SuccessHandler)
        )
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint(authenticationEntryPoint()) // 인증 실패 시 JSON 응답
        );

    http
        .addFilterBefore(new JwtFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)
        .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint() {
    return (HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) -> {
      String errorMessage;

      // 예외 메시지를 기반으로 구체적인 에러 메시지 설정
      if (authException.getCause() instanceof ExpiredJwtException) {
        errorMessage = "토큰이 만료되었습니다. 다시 로그인하세요.";
      } else if (authException.getMessage().contains("JWT")) {
        errorMessage = "잘못된 JWT 토큰입니다.";
      } else {
        String error = String.valueOf(authException.getCause());
        String message = authException.getMessage();
        errorMessage = "인증에 실패했습니다. 로그인 정보를 확인하세요.:"+error+" : "+message;
      }

      response.setContentType("application/json;charset=UTF-8");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

      // 에러 응답 JSON 생성
      response.getWriter().write(String.format("{\"error\": \"Unauthorized\", \"message\": \"%s\"}", errorMessage));
    };
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }


  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    // 허용할 도메인 추가
    configuration.addAllowedOrigin("http://localhost:3000");  // 개발 환경에서 허용
    configuration.addAllowedOrigin("http://transferstay.p-e.kr");  // 배포 환경에서 허용
    configuration.addAllowedOrigin("http://transferstay.p-e.kr:3000");
    configuration.addAllowedMethod("*");  // 모든 HTTP 메소드 허용 (GET, POST, PUT, DELETE 등)
    configuration.addAllowedHeader("*");  // 모든 헤더 허용
    configuration.setAllowCredentials(true);  // 인증 정보 허용 (쿠키, 헤더 등)

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
