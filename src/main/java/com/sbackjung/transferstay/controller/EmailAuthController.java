package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.dto.*;
import com.sbackjung.transferstay.service.EmailSendingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailAuthController {
    private final EmailSendingService emailService;

    @PostMapping("/auth")
    public ResponseEntity<JsonResponse> sendAuthEmail(
           @RequestBody @Valid EmailAuthRequest request
    ){
        System.out.println(request.getEmail());
        EmailAuthResponse response =
                EmailAuthResponse.from(emailService.sendAuthCodeEmail(request.getEmail()));
        JsonResponse jsonResponse = new JsonResponse(HttpStatus.OK.value(), "인증 이메일이 전송되었습니다.", response);
        return ResponseEntity.ok(jsonResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<JsonResponse> checkAuthCode(
            @RequestBody @Valid AuthCodeRequest request
    ){
        AuthCodeResponse response = emailService.checkAuthCode(request.getEmail(), request.getAuthCode());
        JsonResponse jsonResponse = new JsonResponse(HttpStatus.OK.value(), "인증 코드가 확인되었습니다.", response);
        return ResponseEntity.ok(jsonResponse);
    }
}
