package com.sbackjung.transferstay.handler;

import com.sbackjung.transferstay.domain.UserDomain;
import com.sbackjung.transferstay.dto.CustomOAuth2User;
import com.sbackjung.transferstay.jwt.JwtUtils;
import com.sbackjung.transferstay.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
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
        // 저장된 유저를 찾아서, 생성된 인증 토큰을 저장
        Optional<UserDomain> user = userRepository.findByOauthId(oAuthId);
        // 유저 발견 x
        if(user.isEmpty()){
            System.out.println("user not found");
            throw new RuntimeException();
        }
        Long userId = user.get().getUserId();
        String token = jwtUtils.createJwtToken(userId,oAuthId,"user",expiredMs);
        user.get().setAccessToken(token);
        userRepository.save(user.get());
        log.info("success!"+ token);

        //헤더에 추가 todo : 토큰 보여주도록 , Access-Control-Expose-Headers 추가 안하면 헤더에 안보임
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        response.addHeader("Authorization","Bearer "+token);
        response.sendRedirect("http://transferstay.p-e.kr/auth/"+token);
    }
}
