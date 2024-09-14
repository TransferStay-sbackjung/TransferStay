package com.sbackjung.transferstay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CodeValidation {
    private String authorEmail;
    private LocalDateTime createAt;
}
