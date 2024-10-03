package com.sbackjung.transferstay.dto;

import com.sbackjung.transferstay.Enum.PostStatus;
import com.sbackjung.transferstay.domain.AssignmentPost;
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

  @NotNull(message = "투숙 가능 인원을 입력해 주세요.")
  private final int personnel;

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

  private PostStatus status;

  // auction 레코드 생성을 위해
  private final String startDate;
  private final String startTime;
  private final String deadlineDate;
  private final String deadlineTime;
  private final Long startPrice;
  private final Long purchasePrice;

  public AssignmentPost toEntity(Long userId, PostStatus status) {
    return AssignmentPost.builder()
        .userId(userId)
        .title(this.title)
        .price(this.price)
        .description(this.description)
        .isAuction(this.isAuction)
        .locationDepth1(this.locationDepth1)
        .locationDepth2(this.locationDepth2)
        .personnel(this.personnel)
        .reservationPlatform(this.reservationPlatform)
        .checkInDate(this.checkInDate)
        .checkOutDate(this.checkOutDate)
        .reservationCode(this.reservationCode)
        .reservationName(this.reservationName)
        .reservationPhone(this.reservationPhone)
        .status(status)
        .build();
  }
}