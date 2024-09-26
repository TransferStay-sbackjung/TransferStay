package com.sbackjung.transferstay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuctionPostRequestDto implements AuctionDataSetDto{
    @NotNull
    private Long postId;

    @NotBlank
    private String startDate;

    @NotBlank
    private String startTime;

    @NotBlank
    private String deadlineDate;

    @NotBlank
    private String deadlineTime;

    @NotNull
    private Long startPrice;



}
