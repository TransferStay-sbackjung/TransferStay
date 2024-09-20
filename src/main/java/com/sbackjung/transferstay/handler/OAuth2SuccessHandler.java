package com.sbackjung.transferstay.handler;

import com.sbackjung.transferstay.domain.UserDomain;
import com.sbackjung.transferstay.dto.CustomOAuth2User;
import com.sbackjung.transferstay.jwt.JwtUtils;
import com.sbackjung.transferstay.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final Long expiredMs = 10 * 60 * 60 * 1000L;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String oAuthId = oAuth2User.getName();
        System.out.println("oAuthId = " + oAuthId);
        String token = jwtUtils.createJwtToken(oAuthId,"user",expiredMs);
        // 생성된 토큰 저장
        Optional<UserDomain> user = userRepository.findByOauthId(oAuthId);
        if(user.isEmpty()){
            // 에외 처리 추가
            System.out.println("user not found");
            throw new RuntimeException();
        }
        user.get().setAccessToken(token);
        userRepository.save(user.get());

        //헤더에 추가 todo : 토큰 보여주도록
        response.addHeader("Authorization","Bearer "+token);
        response.sendRedirect("http://localhost:8080/auth/"+token);
    }
}
