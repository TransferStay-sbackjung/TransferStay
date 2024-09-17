package com.sbackjung.transferstay.service;

import com.sbackjung.transferstay.domain.UserDomain;
import com.sbackjung.transferstay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepositRechargeService {

  private final UserRepository userRepository;

  // 소셜 로그인 사용자의 충전 처리 (OAuthId 사용)
  public void rechargeDepositByOAuthId(String oauthId, Long amount) {
    UserDomain user = userRepository.findByOauthId(oauthId)
        .orElseThrow(() -> new IllegalArgumentException("해당 OAuth ID를 가진 사용자를 찾을 수 없습니다."));

    // 현재 금액에 충전 금액 추가
    Long currentAmount = user.getAmount();
    user.setAmount(currentAmount + amount);
  }

  // 자체 회원가입 사용자의 충전 처리 (email 사용)
  public void rechargeDepositByEmail(String email, Long amount) {
    UserDomain user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 사용자를 찾을 수 없습니다."));

    // 현재 금액에 충전 금액 추가
    Long currentAmount = user.getAmount();
    user.setAmount(currentAmount + amount);

  }
}