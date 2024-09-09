package com.sbackjung.transferstay.oauth2.service;

import com.sbackjung.transferstay.domain.Role;
import com.sbackjung.transferstay.domain.SocialType;
import com.sbackjung.transferstay.domain.User;
import com.sbackjung.transferstay.oauth2.PrincipalDetails;
import com.sbackjung.transferstay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalOauthUserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthorizationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);

    Map<String, Object> properties = oAuth2User.getAttribute("properties");
    Map<String, Object> account = oAuth2User.getAttribute("kakao_account");

    String providerId = oAuth2User.getAttribute("id").toString();

    String email = (String) account.get("email");
    String name = (String) account.get("name");
    String phone = (String) account.get("phone_number");
    String nickname = (String) account.get("profile_nickname");

    Optional<User> optionalUser = userRepository.findByEmail(email);
    User user;

    // 카카오 네이버 별로 dto 만들예정
    if (optionalUser.isEmpty()) {
      user = User.builder()
          .email(email)
          .name(name)
          .phone(phone)
          .nickname(nickname)
          .role(Role.USER)
          .socialId(providerId)
          .socialType(SocialType.KAKAO) // 추후 수정 ( 구현된 소셜이 kakao 하나다 보니 직접 적음)
          .build();

      userRepository.save(user);
    } else {
      user = optionalUser.get();
    }

    return new PrincipalDetails(user, oAuth2User.getAttributes());
  }
}
