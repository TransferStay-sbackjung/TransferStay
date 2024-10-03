package com.sbackjung.transferstay.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginTestController {
  @GetMapping("/auth/login-success-naver")
  public String testLogin() {
    return "redirection-success";
  }

  @GetMapping("/auth/{token}")
  public String kakaoTestLogin(
      @PathVariable String token
  ) {
    return token;
  }

  @GetMapping("/test/token")
  public String TokenTest() {
    return "토큰인증";
  }
}
