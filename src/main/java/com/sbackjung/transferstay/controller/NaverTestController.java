package com.sbackjung.transferstay.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NaverTestController {
    @GetMapping("/auth/login-success-naver")
    public String testLogin(){
        return "redirection-success";
    }
}
