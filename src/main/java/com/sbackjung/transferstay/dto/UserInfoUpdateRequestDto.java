package com.sbackjung.transferstay.dto;

import com.sbackjung.transferstay.domain.UserDomain;
import lombok.Getter;
import lombok.Setter;

// 수정 예정
@Getter
@Setter
public class UserInfoUpdateRequestDto {
  private String phone;

  public void update(UserDomain user) {
    user.setPhone(this.phone);
  }
}
