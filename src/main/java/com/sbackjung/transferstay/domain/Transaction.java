package com.sbackjung.transferstay.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@DynamicUpdate
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Transaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // 거래 id

  @Column(name = "user_id", nullable = false)
  private Long userId; // 사용자 id (게시글 작성자, 구매자 등)

  @Column(nullable = false)
  private long amount; // 계좌금액?

  @Column(nullable = false)
  private Boolean type; // 송금/입금/충전 정보 -> 3가지를 boolean로 표기할 수 있나..?

  @Column(nullable = false)
  private String description; // 거래명 (입금 / 출금)

  @Column(nullable = false)
  private long balance; // 잔액??

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt; // 거래시간
}
