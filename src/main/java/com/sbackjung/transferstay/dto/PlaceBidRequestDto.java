package com.sbackjung.transferstay.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceBidRequestDto {

  private Long bidderId;      // 입찰자 ID
  private long suggestPrice;  // 제안된 입찰 가격
}
