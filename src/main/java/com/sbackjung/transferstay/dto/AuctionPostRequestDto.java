package com.sbackjung.transferstay.dto;

import com.sbackjung.transferstay.Enum.AuctionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuctionPostRequestDto {
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
