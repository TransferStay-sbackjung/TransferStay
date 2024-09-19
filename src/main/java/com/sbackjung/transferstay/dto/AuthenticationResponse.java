package com.sbackjung.transferstay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticationResponse {
    private boolean isAuth;
    private String message;
    private String email;

}
