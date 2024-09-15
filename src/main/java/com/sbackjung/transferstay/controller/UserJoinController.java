package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.dto.UserJoinForm;
import com.sbackjung.transferstay.dto.UserJoinResponse;
import com.sbackjung.transferstay.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/join")
@Slf4j
public class UserJoinController {
    private final UserService emailLoginService;

    @PostMapping()
    public ResponseEntity<UserJoinResponse> userJoin(
            @RequestBody @Valid UserJoinForm request
    ){
        UserJoinResponse response = emailLoginService.userJoin(request);
        return ResponseEntity.ok(response);
    }
}
