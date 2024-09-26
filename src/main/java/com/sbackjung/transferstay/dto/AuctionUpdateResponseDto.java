package com.sbackjung.transferstay.dto;

import com.sbackjung.transferstay.Enum.AuctionStatus;
import com.sbackjung.transferstay.domain.Auction;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuctionUpdateResponseDto {
    private Long actionId;
    private Long userId;
    private Long postId;
    private LocalDateTime startTime;
    private LocalDateTime deadline;
    private Long startPrice;
    private Long purchasePrice;
    private AuctionStatus status;
    private LocalDateTime updateAt;

    public static AuctionUpdateResponseDto from(Auction auction){
        return AuctionUpdateResponseDto.builder()
                .actionId(auction.getActionId())
                .userId(auction.getUserId())
                .postId(auction.getPostId())
                .startTime(auction.getStartTime())
                .deadline(auction.getDeadline())
                .startPrice(auction.getStartPrice())
                .purchasePrice(auction.getPurchasePrice())
                .status(auction.getStatus())
                .updateAt(LocalDateTime.now())
                .build();
    }
}
