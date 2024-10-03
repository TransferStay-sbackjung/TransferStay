package com.sbackjung.transferstay.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AuctionPurchaseDto {
    private Long postId;
    private Long auctionId;
    private Long buyerId;
    private Long sellerId;
    private Long amount;
}
