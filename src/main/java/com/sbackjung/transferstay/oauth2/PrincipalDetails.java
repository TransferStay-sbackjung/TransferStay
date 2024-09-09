package com.sbackjung.transferstay.oauth2;

import com.sbackjung.transferstay.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

// 추후 자체 회원가입 구현을 위해 PrincipalDetails 을 구현하였음
@Getter
public class PrincipalDetails implements UserDetails, OAuth2User {

  private final User user;
  private Map<String, Object> attributes;

  public PrincipalDetails(User user){
    this.user = user;
  }

  public PrincipalDetails(User user, Map<String, Object> attributes) {
    this.user = user;
    this.attributes = attributes;
  }

  @Override
  public String getName() {
    return user.getEmail();
  }

  @Override
  public Map<String, Object> getAttributes() {
    return Map.of();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(() -> "ROLE_" + user.getRole().name());
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }
}
