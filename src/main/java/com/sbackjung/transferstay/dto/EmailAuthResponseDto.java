package com.sbackjung.transferstay.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmailAuthResponseDto {
    private String to;
    private String code;

    public static EmailAuthResponseDto from(EmailAuthDto dto){
        return EmailAuthResponseDto.builder()
                .to(dto.getTo())
                .code(dto.getCode())
                .build();
    }
}
