package com.sbackjung.transferstay.domain;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "Autionc_transaction")
// 리스너 수정
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

  @LastModifiedDate
  private LocalDateTime updatedAt;

  private BidStatus status;
}
