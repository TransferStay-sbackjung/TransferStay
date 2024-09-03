package com.sbackjung.transferstay.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbackjung.transferstay.domain.UserDomain;
import com.sbackjung.transferstay.dto.CustomOAuth2User;
import com.sbackjung.transferstay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NaverLoginService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request){
        OAuth2User oAuth2User = super.loadUser(request);
        String oauthClientName =
                request.getClientRegistration().getClientName();
        System.out.println(oauthClientName);
        try{
            System.out.println(new ObjectMapper().writeValueAsString(
                    oAuth2User.getAttributes()
            ));
        }catch(Exception e){
            e.printStackTrace();
        }

        UserDomain user = null;
        String userId = null;

        if(oauthClientName.equals("naver")){
            Map<String,String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get(
                    "response");
            //총 36의 길이
            userId = "naver_" + responseMap.get("id").substring(0,30);
            String email = responseMap.get("email");
            user = new UserDomain(userId,email,"naver");
        }

        userRepository.save(user);

        return new CustomOAuth2User(userId);
    }

}
