package com.sbackjung.transferstay.dto;

import com.sbackjung.transferstay.Enum.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssignmentPostRequestDto {

  @NotBlank(message = "제목을 입력해 주세요.")
  private final String title;

  @NotNull(message = "가격을 입력해 주세요.")
  private final Long price;

  @NotBlank(message = "주소를 입력해 주세요.")
  private final String locationDepth1;

  private final String locationDepth2;

  @NotNull(message = "체크인 날짜를 입력해 주세요.")
  private final LocalDate checkInDate;

  @NotNull(message = "체크아웃 날짜를 입력해 주세요.")
  private final LocalDate checkOutDate;

  private final String description;

  @NotBlank(message = "예약 플랫폼을 입력해 주세요.")
  private final String reservationPlatform;

  // todo :  isAuction 필수 값으로 받기? 디폴드 false 로 설정? 여부 의견 묻기!
  @NotNull(message = "경매 여부를 입력해 주세요.")
  private final Boolean isAuction;

  @NotBlank(message = "예약 코드를 입력해 주세요.")
  private final String reservationCode;

  @NotBlank(message = "예약자 이름을 입력해 주세요.")
  private final String reservationName;

  @NotBlank(message = "예약자 전화번호를 입력해 주세요.")
  private final String reservationPhone;

}