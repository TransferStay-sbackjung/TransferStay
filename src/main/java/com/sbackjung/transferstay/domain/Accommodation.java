package com.sbackjung.transferstay.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "accommodation")
@DynamicUpdate

public class Accommodation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "location")
  private String location;

  @Column(name = "reservation_platform")
  private String reservationPlatform;

  @Column(name = "check_in_date")
  private LocalDateTime checkInDate;

  @Column(name = "ckeck_out_date")
  private LocalDateTime ckeckOutDate;

  public void update(String location, LocalDateTime checkInDate, LocalDateTime checkOutDate, String reservationPlatform) {
    this.location = location;
    this.checkInDate = checkInDate;
    this.ckeckOutDate = checkOutDate;
    this.reservationPlatform = reservationPlatform;
  }
}
