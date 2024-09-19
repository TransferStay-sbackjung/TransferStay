package com.sbackjung.transferstay.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmailAuthResponse {
    private String to;
    private String code;

    public static EmailAuthResponse from(EmailAuthDto dto){
        return EmailAuthResponse.builder()
                .to(dto.getTo())
                .code(dto.getCode())
                .build();
    }
}
