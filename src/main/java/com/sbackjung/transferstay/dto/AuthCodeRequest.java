package com.sbackjung.transferstay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthCodeRequest {
    @NotNull
    private String email;
    @NotNull
    private String authCode;
}
