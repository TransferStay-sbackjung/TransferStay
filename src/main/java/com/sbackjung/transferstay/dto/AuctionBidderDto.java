package com.sbackjung.transferstay.dto;

import com.sbackjung.transferstay.domain.AuctionTransaction;
import lombok.*;


import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuctionBidderDto {
    private Long bidderId;
    private Long suggestPrice;
    private LocalDateTime createdAt;

    public static AuctionBidderDto fromEntity(AuctionTransaction auction){
        return AuctionBidderDto.builder()
                .bidderId(auction.getBidderId())
                .suggestPrice(auction.getSuggestPrice())
                .createdAt(auction.getCreatedAt())
                .build();
    }
}
