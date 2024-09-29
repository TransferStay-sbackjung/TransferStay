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

  // 충전
  // todo : 예치금 관련해서 Transaction이 생성되야하지 않을까요?
  @Transactional
  public Long rechargeDeposit(Long userId, Long amount) {
    // 충전 금액 유효성 검사
    if (amount < 1000 || amount % 1000 != 0) {
      throw new CustomException(ErrorCode.INVALID_AMOUNT);
    }
    // 최대 금액 설정
    final Long MAX_AMOUNT = 1_000_000_000L;

    UserDomain user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Long currentAmount = user.getAmount();

    if (currentAmount + amount > MAX_AMOUNT) {
      throw new CustomException(ErrorCode.EXCEEDS_MAX_AMOUNT, "충전 금액이 최대 한도를 초과할 수 없습니다.");
    }

    user.setAmount(currentAmount + amount);

    return currentAmount + amount;
  }

  // 환불
  @Transactional
  public void refundDeposit(Long userId, Long amount) {
    UserDomain user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    if (user.getAmount() < amount) {
      throw new CustomException(ErrorCode.INSUFFICIENT_FUNDS);
    }

    user.setAmount(user.getAmount() - amount);
  }

  // 잔액 조회
  public Long getDepositBalance(Long userId) {
    UserDomain user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    return user.getAmount();  // 현재 잔액 반환
  }
}