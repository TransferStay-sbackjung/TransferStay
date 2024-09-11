package com.sbackjung.transferstay.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentPostUpdateRequestDto {
  private String title;
  private Long price;
  private String description;
  private boolean isAuction;
  private String locationDepth1;
  private String locationDepth2;
  private String reservationPlatform;
  private LocalDate checkInDate;
  private LocalDate checkOutDate;
  private String reservationCode;
  private String reservationName;
  private String reservationPhone;
  private String status;
}
