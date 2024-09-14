package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.dto.AuthCodeRequest;
import com.sbackjung.transferstay.dto.AuthCodeResponse;
import com.sbackjung.transferstay.dto.EmailAuthRequest;
import com.sbackjung.transferstay.dto.EmailAuthResponse;
import com.sbackjung.transferstay.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailAuthController {
    private final EmailService emailService;

    @PostMapping("/auth")
    public ResponseEntity<EmailAuthResponse> sendAuthEmail(
           @RequestBody @Valid EmailAuthRequest request
    ){
        System.out.println(request.getEmail());
        EmailAuthResponse response =
                EmailAuthResponse.from(emailService.sendAuthCodeEmail(request.getEmail()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthCodeResponse> checkAuthCode(
            @RequestBody @Valid AuthCodeRequest request
    ){
        AuthCodeResponse response = emailService.checkAuthCode(request.getEmail(), request.getAuthCode());
        return ResponseEntity.ok(response);
    }
}
