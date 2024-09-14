package com.sbackjung.transferstay.domain;

import com.sbackjung.transferstay.Enum.PostStatus;
import com.sbackjung.transferstay.dto.AssignmentPostUpdateRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "assignment_post")
@DynamicUpdate
public class AssignmentPost {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  @Column(name = "user_id", nullable = false)
  // 임시고정 userId
  private Long userId;


  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private long price;

  @Column(name = "is_auction", nullable = false)
  private boolean isAuction;

  @Column(name = "location_depth1")
  private String locationDepth1;

  @Column(name = "location_depth2")
  private String locationDepth2;

  @Column(name = "reservation_platform")
  private String reservationPlatform;

  @Column(name = "check_in_date")
  private LocalDate checkInDate;

  @Column(name = "check_out_date")
  private LocalDate checkOutDate;

  @Column(name = "reservation_code")
  private String reservationCode;

  @Column(name = "reservation_name")
  private String reservationName;

  @Column(name = "reservation_phone")
  private String reservationPhone;

  @Setter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PostStatus status;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public void update(AssignmentPostUpdateRequestDto dto) {
    if (dto.getTitle() != null) {
      this.title = dto.getTitle();
    }
    if (dto.getPrice() != 0) {
      this.price = dto.getPrice();
    }
    if (dto.getDescription() != null) {
      this.description = dto.getDescription();
    }
    this.isAuction = dto.isAuction();
    if (dto.getLocationDepth1() != null) {
      this.locationDepth1 = dto.getLocationDepth1();
    }
    if (dto.getLocationDepth2() != null) {
      this.locationDepth2 = dto.getLocationDepth2();
    }
    if (dto.getReservationPlatform() != null) {
      this.reservationPlatform = dto.getReservationPlatform();
    }
    if (dto.getCheckInDate() != null) {
      this.checkInDate = dto.getCheckInDate();
    }
    if (dto.getCheckOutDate() != null) {
      this.checkOutDate = dto.getCheckOutDate();
    }
    if (dto.getReservationName() != null) {
      this.reservationName = dto.getReservationName();
    }
    if (dto.getReservationPhone() != null) {
      this.reservationPhone = dto.getReservationPhone();
    }
    if (dto.getReservationCode() != null) {
      this.reservationCode = dto.getReservationCode();
    }
    if (dto.getStatus() != null) {
      this.status = dto.getStatus();
    }
  }


}