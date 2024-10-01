package com.sbackjung.transferstay.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.domain.UserDomain;
import com.sbackjung.transferstay.dto.UserDetailsDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        // 토큰 여부 형식 검증
        if(authorization == null || !authorization.startsWith("Bearer ")){
            log.error("Bearer Token does not Exist.");
            filterChain.doFilter(request,response);
            return;
        }

        // 토큰 값 추출 및 만료시간 검증
        String token = authorization.split(" ")[1];
        Long userId;
        String email;
        String role;

        try {
            if (jwtUtils.isExpiredToken(token)) {
                log.error("Token Expired.");
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Expired token.");
                return;
            }
            userId = jwtUtils.getUserId(token);
            email = jwtUtils.getEmail(token);
            role = jwtUtils.getRole(token);
        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token expired.");
            return;
        } catch (JwtException ex) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Invalid token.");
            return;
        }

        UserDomain userDomain = new UserDomain();
        userDomain.setUserId(userId);
        userDomain.setEmail(email);
        userDomain.setPassword("temp");
        userDomain.setRole(role);

        UserDetailsDto userDetail = new UserDetailsDto(userDomain);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetail,null,userDetail.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // 회원가입, OAuth 경로에 대해 필터링하지 않음
        return path.startsWith("/oauth2/authorization/") ||
                path.equals("/api/test") ||
                path.startsWith("/api/v1/user/join");
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status.value());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), Collections.singletonMap("error", message));
    }
}
