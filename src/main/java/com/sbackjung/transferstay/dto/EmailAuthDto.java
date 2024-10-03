package com.sbackjung.transferstay.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailAuthDto {
    private String to;
    private String code;
}
