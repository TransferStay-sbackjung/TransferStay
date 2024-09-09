package com.sbackjung.transferstay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuth2TestController {
  @GetMapping("/auth/success")
  public String home() {
    return "success";
  }
}