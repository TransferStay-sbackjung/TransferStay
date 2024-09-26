package com.sbackjung.transferstay.domain;

import com.sbackjung.transferstay.Enum.AuctionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long actionId;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(nullable = false)
    private Long startPrice;

    @Column(nullable = false)
    private Long purchasePrice;

    private Long winningBidderId;

    private Long winningPrice;

    @Enumerated(EnumType.STRING)
    private AuctionStatus status;
}
