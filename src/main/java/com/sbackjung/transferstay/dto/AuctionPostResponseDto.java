package com.sbackjung.transferstay.dto;

import com.sbackjung.transferstay.Enum.AuctionStatus;
import com.sbackjung.transferstay.domain.Auction;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuctionPostResponseDto {
    private Long actionId;
    private Long userId;
    private Long postId;
    private LocalDateTime startTime;
    private LocalDateTime deadline;
    private Long startPrice;
    private AuctionStatus status;

    public static AuctionPostResponseDto from(Auction auction){
        return AuctionPostResponseDto.builder()
                .actionId(auction.getActionId())
                .userId(auction.getUserId())
                .postId(auction.getPostId())
                .startTime(auction.getStartTime())
                .deadline(auction.getDeadline())
                .startPrice(auction.getStartPrice())
                .status(auction.getStatus())
                .build();
    }
}
