package com.sbackjung.transferstay.dto;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthCodeResponse {
    @NotNull
    private String email;
    @NotNull
    private boolean isAuth;
}
