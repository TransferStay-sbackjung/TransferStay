package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.dto.JsonResponse;
import com.sbackjung.transferstay.dto.UserJoinForm;
import com.sbackjung.transferstay.dto.UserJoinResponse;
import com.sbackjung.transferstay.service.UserJoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/join")
@Slf4j
@Tag(name = "Auth")
public class UserJoinController {
    private final UserJoinService emailLoginService;

    @Operation(summary = "이메일 회원 가입", description = "이메일 회원가입을 진행합니다.")
    @PostMapping()
    public ResponseEntity<JsonResponse> userJoin(
            @RequestBody @Valid UserJoinForm request
    ){
        UserJoinResponse userJoinResponse = emailLoginService.userJoin(request);
        JsonResponse response = new JsonResponse(HttpStatus.OK.value(), "회원가입이 완료되었습니다.", userJoinResponse);
        return ResponseEntity.ok(response);
    }
}
