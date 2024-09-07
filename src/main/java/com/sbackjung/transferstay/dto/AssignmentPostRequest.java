package com.sbackjung.transferstay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssignmentPostRequest {

  @NotBlank(message = "제목을 입력해 주세요.")
  private final String title;

  @NotNull(message = "가격을 입력해 주세요.")
  private final BigDecimal price;

  @NotBlank(message = "주소를 입력해 주세요.")
  private final String location;

  @NotNull(message = "체크인 날짜를 입력해 주세요.")
  private final LocalDateTime checkInDate;

  @NotNull(message = "체크아웃 날짜를 입력해 주세요.")
  private final LocalDateTime checkOutDate;

  private final String description;

  @NotBlank(message = "예약 플랫폼을 입력해 주세요.")
  private final String reservationPlatform;

  private final boolean isAuction;
}