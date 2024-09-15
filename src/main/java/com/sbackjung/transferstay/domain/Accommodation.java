package com.sbackjung.transferstay.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "accommodation")


public class Accommodation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="title")
  private String title;

  @Column(name = "location_depth1")
  private String locationDepth1;

  @Column(name = "location_depth2")
  private String locationDepth2;

  @Column(name = "reservation_platform")
  private String reservationPlatform;

  @Column(name = "check_in_date")
  private LocalDate checkInDate;

  @Column(name = "ckeck_out_date")
  private LocalDate checkOutDate;

  @Column(name = "personnel")
  private int personnel;

  @Column(name = "price")
  private double price;


}
