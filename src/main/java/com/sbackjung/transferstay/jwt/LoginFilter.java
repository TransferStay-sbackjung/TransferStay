package com.sbackjung.transferstay.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbackjung.transferstay.dto.AuthenticationResponse;
import com.sbackjung.transferstay.dto.UserDetailsDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.OnClose;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final Long expiredMs = 10 * 60 * 60 * 1000L;

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
            throw new RuntimeException(e);
        }
        String email = requestMap.get("email");
        String password = requestMap.get("password");
        log.info("user email {} ",email);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email,password,null);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authentication
    ) throws IOException {
        UserDetailsDto user = (UserDetailsDto)authentication.getPrincipal();

        String email = user.getEmail();

        Collection<? extends GrantedAuthority> authorities =
                authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator =
                authorities.iterator();

        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtils.createJwtToken(email,role,expiredMs);

        AuthenticationResponse authResponse = new AuthenticationResponse(true
                , "인증이 완료되었습니다.", email);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader("Authorization", "Bearer " + token);
        response.setStatus(HttpStatus.OK.value());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), authResponse);
    }
}
