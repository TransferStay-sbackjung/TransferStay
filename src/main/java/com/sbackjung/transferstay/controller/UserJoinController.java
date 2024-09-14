package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.service.EmailLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/join")
@Slf4j
public class UserJoinController {
    private final EmailLoginService emailLoginService;

    @PostMapping()
    public void userJoin(
            @RequestBody
    ){

    }
}
