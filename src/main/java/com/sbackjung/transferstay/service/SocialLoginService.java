package com.sbackjung.transferstay.service;

import com.sbackjung.transferstay.domain.UserDomain;
import com.sbackjung.transferstay.dto.CustomOAuth2User;
import com.sbackjung.transferstay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocialLoginService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest request) {
    OAuth2User oAuth2User = super.loadUser(request);
    String oauthClientName =
        request.getClientRegistration().getClientName();
    // 출력테스트용
//        System.out.println(oauthClientName);
//        try{
//            System.out.println(new ObjectMapper().writeValueAsString(
//                    oAuth2User.getAttributes()
//            ));
//        }catch(Exception e){
//            e.printStackTrace();
//        }

    UserDomain user = null;
    String oAuthId = null;

    // 네이버
    if (oauthClientName.equals("naver")) {
      Map<String, String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get(
          "response");
      //총 36의 길이
      oAuthId = "naver_" + responseMap.get("id").substring(0, 30);
      String email = responseMap.get("email");
      Optional<UserDomain> byEmail = userRepository.findByEmail(email);
      // 해당 이메일이 존재한다면,
      if (byEmail.isPresent()) {
        System.out.println("email is exist");
        return new CustomOAuth2User(oAuthId);
      } else {
        System.out.println("user not exist! create User and save DB " +
            "before add token");
        user = new UserDomain(oAuthId, email, "naver");
        userRepository.save(user);
      }
    }
    // 카카오
    else if (oauthClientName.equals("kakao")) {

      // 카카오 사용자 정보에서 id와 이메일 추출
      Map<String, Object> kakaoAccountMap = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
      String id = oAuth2User.getAttributes().get("id").toString();
      if (id.length() > 30) {
        oAuthId = "naver_" + id.substring(0, 30);
      } else {
        oAuthId = "naver_" + id;
      }
      String email = (String) kakaoAccountMap.get("email");

      Optional<UserDomain> byEmail = userRepository.findByEmail(email);
      // 해당 이메일이 존재한다면,
      if (byEmail.isPresent()) {
        System.out.println("email is exist");
        return new CustomOAuth2User(oAuthId);
      } else {
        System.out.println("user not exist! create User and save DB before add token");
        user = new UserDomain(oAuthId, email, "kakao");
        userRepository.save(user);
      }
    }
    return new CustomOAuth2User(oAuthId);
  }
}
