package com.sbackjung.transferstay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuctionUpdateRequestDto implements AuctionDataSetDto{
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
