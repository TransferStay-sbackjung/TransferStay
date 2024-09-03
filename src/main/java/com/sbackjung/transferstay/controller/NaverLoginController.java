package com.sbackjung.transferstay.controller;

import com.sbackjung.transferstay.service.NaverLoginService;
import jakarta.servlet.http.HttpServlet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NaverLoginController {

    private final NaverLoginService naverLoginService;
}
