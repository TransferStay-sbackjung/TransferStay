package com.sbackjung.transferstay.jwt;

import com.sbackjung.transferstay.dto.UserDetailsDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        // 토큰 여부 형식 검증
        if(authorization == null || !authorization.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        // 토큰 값 추출 및 만료시간 검증
        String token = authorization.split(" ")[1];
        if(jwtUtils.isExpiredToken(token)){
            // todo : 에러 형식으로 전환
            throw new RuntimeException("token 만료");
        }

        String email = jwtUtils.getUserEmail(token);
        String role = jwtUtils.getRole(token);

        UserDetailsDto userDetail = new UserDetailsDto(email,"",role);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetail,null,userDetail.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request,response);
    }
}
