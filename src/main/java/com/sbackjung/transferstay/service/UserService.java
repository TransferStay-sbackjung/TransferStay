package com.sbackjung.transferstay.service;

import com.sbackjung.transferstay.config.exception.CustomException;
import com.sbackjung.transferstay.config.exception.ErrorCode;
import com.sbackjung.transferstay.domain.UserDomain;
import com.sbackjung.transferstay.dto.UserInfoResponseDto;
import com.sbackjung.transferstay.dto.UserInfoUpdateRequestDto;
import com.sbackjung.transferstay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  // 사용자 정보 조회
  public UserInfoResponseDto getUserInfo(Long userId) {
    UserDomain user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    return UserInfoResponseDto.from(user);
  }

  // 사용자 정보 수정
  @Transactional
  public void updateUserInfo(Long userId, UserInfoUpdateRequestDto request) {
    UserDomain user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    request.update(user);
  }
}
