package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.dto.EmailAuthDto;
import com.sbackjung.transferstay.dto.EmailAuthRequestDto;
import com.sbackjung.transferstay.dto.EmailAuthResponseDto;
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
    public ResponseEntity<EmailAuthResponseDto> sendAuthEmail(
           @RequestBody @Valid EmailAuthRequestDto request
    ){
        System.out.println(request.getEmail());
        EmailAuthResponseDto response =
                EmailAuthResponseDto.from(emailService.sendEmail(request.getEmail()));
        return ResponseEntity.ok(response);
    }
}
