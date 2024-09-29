package com.sbackjung.transferstay.dto;

import com.sbackjung.transferstay.domain.UserDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 수정 예정
@Getter
@AllArgsConstructor
public class UserInfoResponseDto {
  private Long userId;
  private Long amount;
  private String email;
  private String phone;

  public static UserInfoResponseDto from(UserDomain user) {
    return new UserInfoResponseDto(user.getUserId(),user.getAmount(), user.getEmail(),
            user.getPhone());
  }
}
