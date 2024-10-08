package com.sbackjung.transferstay.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.dto.AuthenticationResponse;
import com.sbackjung.transferstay.dto.UserDetailsDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final Long expiredMs = 10 * 60 * 60 * 1000L;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,String> requestMap;

        try {
            requestMap = objectMapper.readValue(request.getInputStream(),
                    Map.class);
        } catch (IOException e) {
            // todo: 로그인 에러 추가
            throw new CustomException(
                    ErrorCode.BAD_REQUEST, "이메일과 패스워드를 옳바르게 입력해주세요.");
        }

        String email = requestMap.get("email");
        String password = requestMap.get("password");
        log.info("user email {} ",email);
        log.info("user password {} ",password);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email,password,
                        Collections.emptyList());
        System.out.println(authToken);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authentication
    ) throws IOException {
        UserDetailsDto user = (UserDetailsDto) authentication.getPrincipal();

        String email = user.getUsername();
        Long userId = user.getUserId();

        log.info("email = {}",email);
        Collection<? extends GrantedAuthority> authorities =
                authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator =
                authorities.iterator();

        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtils.createJwtToken(userId,email,role,expiredMs);
        log.info(token);

        AuthenticationResponse authResponse = new AuthenticationResponse(true
                , "Authentication Success.", email);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        response.setHeader("Authorization", "Bearer " + token);
        response.setStatus(HttpStatus.OK.value());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), authResponse);
    }

    //인증실패시 동작 부분
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        String message;

        if (failed instanceof BadCredentialsException) {
            message = "잘못된 정보입니다.";
        } else if (failed instanceof UsernameNotFoundException) {
            message = "유저 정보가 잘못되었습니다.";
        } else if(failed instanceof InternalAuthenticationServiceException){
            message = "유저 정보가 잘못되었습니다.";
        } else {
            message = "인증에 실패했습니다.";
        }

        sendErrorResponse(response, HttpStatus.UNAUTHORIZED, message);
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status.value());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), Collections.singletonMap("error", message));
    }
}
