package com.sbackjung.transferstay.dto;

import com.sbackjung.transferstay.Enum.AuctionStatus;
import com.sbackjung.transferstay.domain.Auction;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuctionGetListDto {
    private Long actionId;
    private Long postId;
    private Long userId;
    private LocalDateTime startTime;
    private LocalDateTime deadline;
    private Long startPrice;
    private Long winningBidderId;
    private Long winningPrice;
    private AuctionStatus status;

    public static AuctionGetListDto from(Auction auction){
        return AuctionGetListDto.builder()
                .actionId(auction.getActionId())
                .postId(auction.getPostId())
                .userId(auction.getUserId())
                .startTime(auction.getStartTime())
                .deadline(auction.getDeadline())
                .startPrice(auction.getStartPrice())
                .winningBidderId(auction.getWinningBidderId())
                .winningPrice(auction.getWinningPrice())
                .status(auction.getStatus())
                .build();
    }
}
