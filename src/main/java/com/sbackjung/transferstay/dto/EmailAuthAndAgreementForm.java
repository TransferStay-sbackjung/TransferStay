package com.sbackjung.transferstay.dto;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailAuthAndAgreementForm {
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String passwordCheck;
    @NotNull
    private String phoneNumber;
}
