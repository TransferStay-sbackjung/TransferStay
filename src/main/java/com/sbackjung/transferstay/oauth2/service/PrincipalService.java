package com.sbackjung.transferstay.oauth2.service;

import com.sbackjung.transferstay.domain.User;
import com.sbackjung.transferstay.oauth2.PrincipalDetails;
import com.sbackjung.transferstay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 자체 로그인 전용 CustomUserDetailService
 */
@Service
@RequiredArgsConstructor
public class PrincipalService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<User> findUser = userRepository.findByEmail(email);

    // 사용자 존재 여부 확인
    return findUser.map(PrincipalDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
  }
}