package com.sbackjung.transferstay.config;

import com.sbackjung.transferstay.handler.OAuth2SuccessHandler;
import com.sbackjung.transferstay.jwt.JwtFilter;
import com.sbackjung.transferstay.jwt.JwtUtils;
import com.sbackjung.transferstay.jwt.LoginFilter;
import com.sbackjung.transferstay.service.EmailLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private final DefaultOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final EmailLoginService emailLoginService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        LoginFilter loginFilter = new LoginFilter(authManager, jwtUtils);
        loginFilter.setFilterProcessesUrl("/email-login");

        http
                .csrf(csrf -> csrf.disable())
                .formLogin(auth -> auth.disable())
                .httpBasic(auth -> auth.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/", "/login/oauth2/**", "/h2-console/**", "/auth/login-success-naver", "/auth/login-success-kakao").permitAll()
                        .requestMatchers("/email-login").permitAll()
                        .requestMatchers("/api/v1/user/join").permitAll()
                        .requestMatchers("/api/v1/email/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/login/oauth2/code/{registrationId}"))
                        .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                );

        http
                .addFilterBefore(new JwtFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
