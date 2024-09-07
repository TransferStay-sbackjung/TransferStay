package com.sbackjung.transferstay.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssignmentPostResponse {
  private final Long id;
  private final String title;
  private final BigDecimal price;
  private final String description;
  private final boolean isAuction;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;

  //Accommodation
  private final String location;
  private final LocalDateTime checkInDate;
  private final LocalDateTime checkOutDate;
  private final String reservationPlatform;

}