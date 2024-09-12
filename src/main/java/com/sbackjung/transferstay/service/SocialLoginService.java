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
  public static final String NAVER = "naver";
  public static final String KAKAO = "kakao";

  @Override
  public OAuth2User loadUser(OAuth2UserRequest request) {
    OAuth2User oAuth2User = super.loadUser(request);
    String oauthClientName =
        request.getClientRegistration().getClientName();
    System.out.println(oauthClientName);

    String oAuthId = null;
    CustomOAuth2User oAuthUser = null;
    // 네이버 로그인 
    if (oauthClientName.equals(NAVER)) {
      Map<String, String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get(
          "response");
      //총 36의 길이
      oAuthId = NAVER + "_" + responseMap.get("id").substring(0, 30);
      String email = responseMap.get("email");
      oAuthUser = checkUserExistElseCreate(oAuthId,email,NAVER);
    }
    // 카카오 로그인
    else if (oauthClientName.equals(KAKAO)) {
      // 카카오 사용자 정보에서 id와 이메일 추출
      Map<String, Object> kakaoAccountMap = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
      String id = oAuth2User.getAttributes().get("id").toString();
      System.out.println("id = " + id);
      if (id.length() > 30) {
        oAuthId = KAKAO + "_" + id.substring(0, 30);
      } else {
        oAuthId = KAKAO + "_" + id;
      }
      String email = (String) kakaoAccountMap.get("email");
      System.out.println("email = " + email);
      oAuthUser = checkUserExistElseCreate(oAuthId,email,KAKAO);
    }
    return oAuthUser;
  }

  private CustomOAuth2User checkUserExistElseCreate(String oAuthId,String email,
                                        String provider){
    Optional<UserDomain> byEmail = userRepository.findByEmail(email);
    // 해당 이메일이 존재한다면,
    if (byEmail.isPresent()) {
      // todo : 해당 부분 기존에 존재하는 계정으로 로그인하도록 설절
      System.out.println("email is exist oauthid"+byEmail.get().getOauthId());
      return new CustomOAuth2User(byEmail.get().getOauthId());
    } else {
      System.out.println("user not exist! create User and save DB before add token");
      UserDomain user = new UserDomain(oAuthId, email, provider);
      userRepository.save(user);
      return new CustomOAuth2User(oAuthId);
    }
  }
}
