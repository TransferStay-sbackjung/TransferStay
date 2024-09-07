package com.sbackjung.transferstay.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
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
//import org.springframework.data.annotation.Id;
import jakarta.persistence.Id;
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
  // 숙소 정보
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "accommodation_id", nullable = false)
  private Accommodation accommodation;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(name = "is_auction", nullable = false)
  private boolean isAuction;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public void update(String title, BigDecimal price, String description, boolean isAuction) {
    this.title = title;
    this.price = price;
    this.description = description;
    this.isAuction = isAuction;
  }

  public void updateAccommodation(String location, LocalDateTime checkInDate, LocalDateTime checkOutDate, String reservationPlatform) {
    this.accommodation.update(location, checkInDate, checkOutDate, reservationPlatform);
  }
}