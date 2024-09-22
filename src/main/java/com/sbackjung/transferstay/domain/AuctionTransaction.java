package com.sbackjung.transferstay.domain;

import com.sbackjung.transferstay.Enum.AuctionStatus;
import com.sbackjung.transferstay.Enum.BidStatus;
import com.sbackjung.transferstay.Enum.BidType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "Autionc_transaction")
@EntityListeners(EntityListeners.class)

public class AuctionTransaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // 입찰 내역 id

  private Long auctionId;

  private Long bidderId;

  @Column(nullable = false)
  private Long suggestPrice;

  private Long maxPrice;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private BidType type;

  @CreatedDate
  private LocalDateTime createdAt;

  private BidStatus status;
}
