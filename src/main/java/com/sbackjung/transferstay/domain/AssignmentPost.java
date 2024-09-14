package com.sbackjung.transferstay.domain;

import com.sbackjung.transferstay.dto.AssignmentPostUpdateRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
  private Long id;  // post_id


  @Column(name = "user_id", nullable = false)
  // 임시고정 userId
  private Long userId;

  /**

  * 추후 user 테이블 생성하면 변경하기

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  **/

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private Long price;

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

  @Column(name = "ckeck_out_date")
  private LocalDate ckeckOutDate;

  @Column(name = "reservation_code")
  private String reservationCode;

  @Column(name = "reservation_name")
  private String reservationName;

  @Column(name = "reservation_phone")
  private String reservationPhone;

  @Column(name = "status")
  private String status;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public void update(AssignmentPostUpdateRequestDto dto) {
    this.title = dto.getTitle();
    this.price = dto.getPrice();
    this.description = dto.getDescription();
    this.isAuction = dto.isAuction();
    this.locationDepth1 = dto.getLocationDepth1();
    this.locationDepth2 = dto.getLocationDepth2();
    this.reservationPlatform = dto.getReservationPlatform();
    this.checkInDate = dto.getCheckInDate();
    this.ckeckOutDate = dto.getCheckOutDate();
    this.reservationName = dto.getReservationName();
    this.reservationPhone = dto.getReservationPhone();
    this.reservationCode = dto.getReservationCode();
    this.status = dto.getStatus();
  }

}