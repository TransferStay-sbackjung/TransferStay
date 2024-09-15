package com.sbackjung.transferstay.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserJoinResponse {
    private String email;
    private Long userId;
}
