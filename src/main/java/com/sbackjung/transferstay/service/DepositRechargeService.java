package com.sbackjung.transferstay.service;

import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.domain.UserDomain;
import com.sbackjung.transferstay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepositRechargeService {

  private final UserRepository userRepository;

  // 소셜 로그인 사용자의 충전 처리 (OAuthId 사용)
  @Transactional
  public void rechargeDepositByOAuthId(String oauthId, Long amount) {

    // 충전 금액 에러처리
    if (amount < 1000) {
      throw new CustomException(ErrorCode.INVALID_AMOUNT);  // 0 이하 금액 처리
    } else if (amount % 1000 != 0) {
      throw new CustomException(ErrorCode.INVALID_AMOUNT);
    }

    UserDomain user = userRepository.findByEmailWithLock(oauthId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    // 현재 금액에 충전 금액 추가
    Long currentAmount = user.getAmount();
    user.setAmount(currentAmount + amount);
  }

  // 자체 회원가입 사용자의 충전 처리 (email 사용)
  @Transactional
  public void rechargeDepositByEmail(String email, Long amount) {

    // 충전 금액 에러처리
    if (amount < 1000) {
      throw new CustomException(ErrorCode.INVALID_AMOUNT);  // 0 이하 금액 처리
    } else if (amount % 1000 != 0) {
      throw new CustomException(ErrorCode.INVALID_AMOUNT);
    }

    UserDomain user = userRepository.findByEmailWithLock(email)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    // 현재 금액에 충전 금액 추가
    Long currentAmount = user.getAmount();
    user.setAmount(currentAmount + amount);

  }
}