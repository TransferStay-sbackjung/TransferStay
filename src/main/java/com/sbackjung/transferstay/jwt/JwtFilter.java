package com.sbackjung.transferstay.jwt;

import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.domain.UserDomain;
import com.sbackjung.transferstay.dto.UserDetailsDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
        if(jwtUtils.isExpiredToken(token)){
            // todo : 에러 형식으로 전환
            log.error("Token Expire.");
            throw new CustomException(
                    ErrorCode.UN_AUTHORIZE, "만료된 토큰입니다.");
        }

        String email = jwtUtils.getUserId(token);   
        String role = jwtUtils.getRole(token);

        UserDomain userDomain = new UserDomain();
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
}
