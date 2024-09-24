package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.dto.*;
import com.sbackjung.transferstay.service.EmailSendingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth", description = "회원관리, 로그인 관련 API")
public class EmailAuthController {
    private final EmailSendingService emailService;

    @Operation(summary = "이메일 인증 코드 전송", description = "사용자의 이메일로 인증 코드를 전송합니다.")
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

    @Operation(summary = "이메일 인증 코드 확인", description = "사용자의 인증 코드를 확인합니다.")
    @PostMapping("/verify")
    public ResponseEntity<JsonResponse> checkAuthCode(
            @RequestBody @Valid AuthCodeRequest request
    ){
        AuthCodeResponse response = emailService.checkAuthCode(request.getEmail(), request.getAuthCode());
        JsonResponse jsonResponse = new JsonResponse(HttpStatus.OK.value(), "인증 코드가 확인되었습니다.", response);
        return ResponseEntity.ok(jsonResponse);
    }
}
