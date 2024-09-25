package com.sbackjung.transferstay.dto;

import com.sbackjung.transferstay.Enum.BidType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceBidRequestDto {
  private long suggestPrice;  // 제안된 입찰 가격
  private BidType bidType;    // 입찰 타입
  private Long MaxPrice;      // 자동 시 최고금액 설정
}
