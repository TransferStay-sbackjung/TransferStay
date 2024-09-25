package com.sbackjung.transferstay.dto;

import com.sbackjung.transferstay.Enum.PostStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sbackjung.transferstay.domain.AssignmentPost;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssignmentPostResponseDto {
  private final Long id;
  private final String title;
  private final Long price;
  private final String description;
  private final boolean isAuction;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;

  // Location 관련 필드
  private final String locationDepth1;
  private final String locationDepth2;

  // 예약 관련 필드
  private final LocalDate checkInDate;
  private final LocalDate checkOutDate;
  private final String reservationPlatform;
  private final String reservationCode;
  private final String reservationName;
  private final String reservationPhone;

  // 상태
  private final PostStatus status;

  public static AssignmentPostResponseDto fromEntity(AssignmentPost assignmentPost) {
    return AssignmentPostResponseDto.builder()
        .id(assignmentPost.getId())
        .title(assignmentPost.getTitle())
        .price(assignmentPost.getPrice())
        .description(assignmentPost.getDescription())
        .isAuction(assignmentPost.isAuction())
        .locationDepth1(assignmentPost.getLocationDepth1())
        .locationDepth2(assignmentPost.getLocationDepth2())
        .reservationPlatform(assignmentPost.getReservationPlatform())
        .checkInDate(assignmentPost.getCheckInDate())
        .checkOutDate(assignmentPost.getCheckOutDate())
        .reservationCode(assignmentPost.getReservationCode())
        .reservationName(assignmentPost.getReservationName())
        .reservationPhone(assignmentPost.getReservationPhone())
        .status(assignmentPost.getStatus())
        .createdAt(assignmentPost.getCreatedAt())
        .updatedAt(assignmentPost.getUpdatedAt())
        .build();
  }
}
