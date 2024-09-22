package com.sbackjung.transferstay.service;

import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.domain.UserDomain;
import com.sbackjung.transferstay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
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

    log.info(oauthId);
    UserDomain user = userRepository.findByOauthId(oauthId)
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

    UserDomain user = userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    // 현재 금액에 충전 금액 추가
    Long currentAmount = user.getAmount();
    user.setAmount(currentAmount + amount);

  }
  // 환불 처리
  @Transactional
  public void refundDepositByOAuthId(String oauthId, Long amount) {
    UserDomain user = userRepository.findByOauthId(oauthId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    if (user.getAmount() < amount) {
      throw new CustomException(ErrorCode.INSUFFICIENT_FUNDS);
    }

    user.setAmount(user.getAmount() - amount);
  }

  @Transactional
  public void refundDepositByEmail(String email, Long amount) {
    UserDomain user = userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    if (user.getAmount() < amount) {
      throw new CustomException(ErrorCode.INSUFFICIENT_FUNDS);
    }

    user.setAmount(user.getAmount() - amount);
  }
}